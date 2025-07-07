package com.ecommerceservice.customers.service;

import com.ecommerceservice.customers.dao.CartItemDao;
import com.ecommerceservice.customers.dao.WishlistItemDao;
import com.ecommerceservice.customers.mapper.CartMapper;
import com.ecommerceservice.customers.model.request.CartItemRequestDTO;
import com.ecommerceservice.customers.model.request.WishlistItemRequestDTO;
import com.ecommerceservice.customers.model.response.CartDetailsResponseDTO;
import com.ecommerceservice.customers.model.response.WishlistCartCountResponseDto;
import com.ecommerceservice.customers.repository.CartItemRepository;
import com.ecommerceservice.customers.repository.WishListRepository;
import com.ecommerceservice.inventory.dao.Product;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.utility.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.mapper.CustomerMapper;
import com.ecommerceservice.customers.model.request.AddCustomerRequest;
import com.ecommerceservice.customers.model.response.CustomerResponse;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.utility.JdbcUtil;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private WishListRepository wishlistRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    @Override
    public BaseResponse getAllCustomers() {
        List<CustomerDao> customerDaoList = customerRepository.findAll();
        List<CustomerResponse> customerResponses = customerMapper.customersDaosToCustomerResponseList(customerDaoList);
        return BaseResponseUtility.getBaseResponse(customerResponses);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse addCustomer(AddCustomerRequest addCustomerRequest) throws BadRequestException {
        Boolean isEmailExists = customerRepository.existsByEmail(addCustomerRequest.getEmail());
        if(Boolean.TRUE.equals(isEmailExists)){
            throw  new BadRequestException(ExceptionConstants.CUSTOMER_EMAIL_ALREADY_EXISTS);
        }
        Boolean isPhoneNumberExists = customerRepository.existsByPhoneNumber(addCustomerRequest.getPhoneNumber());
        if (Boolean.TRUE.equals(isPhoneNumberExists)){
            throw new BadRequestException(ExceptionConstants.CUSTOMER_PHONE_NUMBER_ALREADY_EXISTS);
        }
        CustomerDao customerDao = customerMapper.customerRequesttoCustomerDao(addCustomerRequest);
        customerDao = customerRepository.save(customerDao);
        return  BaseResponseUtility.getBaseResponse(customerDao);
    }

    @Override
    public BaseResponse getCustomerById(Long customerId) throws BadRequestException {
        CustomerDao customerDao = customerRepository.findByIdAndIsActiveTrue(customerId);
        if(ObjectUtils.isEmpty(customerDao)){
            throw new BadRequestException(ExceptionConstants.INVALID_CUSTOMER);
        }
        return BaseResponseUtility.getBaseResponse(customerDao);
    }

    @Override
    public BaseResponse addToCart(CartItemRequestDTO dto) throws BadRequestException {
        CartItemDao dao = cartItemRepository.findByCustomerIdAndProductId(dto.getCustomerId(),dto.getProductId());
        if(!ObjectUtils.isEmpty(dao)){
            throw new BadRequestException(ExceptionConstants.ALREADY_EXISTS_IN_CART);
        }
        CartItemDao cartItemDao = new CartItemDao();
        cartItemDao.setCustomerId(dto.getCustomerId());
        cartItemDao.setProductId(dto.getProductId());
        cartItemDao.setQuantity(dto.getQuantity());
        cartItemDao.setCreatedAt(LocalDateTime.now());
        cartItemDao.setUpdatedAt(LocalDateTime.now());
        cartItemRepository.save(cartItemDao);
        Long totalCartItems = cartItemRepository.countByCustomerId(dto.getCustomerId());
        return  BaseResponseUtility.getBaseResponse(totalCartItems);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse updateCartItem(List<CartItemRequestDTO> dto) throws BadRequestException {
        List<CartItemDao> cartItemDaos = cartItemRepository.findAllByProductIdInAndCustomerId(dto.stream().map(CartItemRequestDTO::getProductId).toList(),dto.get(0).getCustomerId());
        if(dto.size() != cartItemDaos.size()){
            throw new BadRequestException(ExceptionConstants.INVALID_PRODUCT_IDS);
        }
        Map<Long,Integer> requestedQuantities = dto.stream().collect(Collectors.toMap(CartItemRequestDTO::getProductId,CartItemRequestDTO::getQuantity));
        List<CartItemDao> itemsToDelete = new ArrayList<>();
        List<CartItemDao> itemsToUpdate = new ArrayList<>();

        // 3. Process each existing cart item
        for (CartItemDao cartItem : cartItemDaos) {
            Long productId = cartItem.getProductId();
            Integer requestedQty = requestedQuantities.get(productId);
            if (requestedQty == null) continue;
            if (requestedQty <= 0) {
                itemsToDelete.add(cartItem);
            } else if (!requestedQty.equals(cartItem.getQuantity())) {
                cartItem.setQuantity(requestedQty);
                itemsToUpdate.add(cartItem);
            }
        }
        if (!itemsToUpdate.isEmpty()) {
            cartItemRepository.saveAll(itemsToUpdate);
        }

        if (!itemsToDelete.isEmpty()) {
            cartItemRepository.deleteAll(itemsToDelete);
        }

        return BaseResponseUtility.getBaseResponse("Cart is updated Successfully");
    }

    @Override
    public BaseResponse getCustomerCartDetails(Long customerId) {
        List<CartItemDao> cartItemDao = cartItemRepository.findAllByCustomerId(customerId);
        if(ObjectUtils.isEmpty(cartItemDao)){
            return BaseResponseUtility.getBaseResponse();
        }
        List<CartDetailsResponseDTO> cartDetailsResponseDTOS = cartMapper.cartsDaoToCartsDto(cartItemDao);
        return BaseResponseUtility.getBaseResponse(cartDetailsResponseDTOS);
    }



    @Override
    public BaseResponse addToWishlist(WishlistItemRequestDTO dto) throws BadRequestException {
        requestBodyValidation(dto);
        Optional<WishlistItemDao> existing = wishlistRepository.findByCustomerIdAndProductId(dto.getCustomerId(), dto.getProductId());
        if (existing.isPresent()) {
            throw new BadRequestException(ExceptionConstants.PRODUCT_ALREADY_IN_WISHLIST);
        }
        WishlistItemDao item = new WishlistItemDao();
        item.setCustomerId(dto.getCustomerId());
        item.setProductId(dto.getProductId());
        item.setCreatedAt(LocalDateTime.now());
        wishlistRepository.save(item);
        Long totalWishlistItems = wishlistRepository.countByCustomerId(dto.getCustomerId());
        return BaseResponseUtility.getBaseResponse(totalWishlistItems);
    }


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse removeFromWishlist(WishlistItemRequestDTO dto) throws BadRequestException {
        requestBodyValidation(dto);
        wishlistRepository.deleteByCustomerIdAndProductId(dto.getCustomerId(), dto.getProductId());
        Long totalWishlistItems = wishlistRepository.countByCustomerId(dto.getCustomerId());
        return BaseResponseUtility.getBaseResponse(totalWishlistItems);
    }

    @Override
    public BaseResponse getWishlist(Long customerId) {
        List<WishlistItemDao> items = wishlistRepository.findAllByCustomerId(customerId);
        return BaseResponseUtility.getBaseResponse(items);
    }

    @Override
    public BaseResponse getCustomerCartCount(Long customerId) {
        Long count = cartItemRepository.countByCustomerId(customerId);
        return BaseResponseUtility.getBaseResponse(count);
    }

    @Override
    public BaseResponse getWishlistCount(Long customerId) {
        Long count = wishlistRepository.countByCustomerId(customerId);
        return BaseResponseUtility.getBaseResponse(count);
    }

    @Override
    public BaseResponse getWishlistCartCount(Long customerId) {
        WishlistCartCountResponseDto dto = jdbcUtil.getWishlistCartCountDetails(customerId);
        return BaseResponseUtility.getBaseResponse(dto);
    }

    public void requestBodyValidation(WishlistItemRequestDTO dto) throws BadRequestException {
        CustomerDao customerDao = customerRepository.findByIdAndIsActiveTrue(dto.getCustomerId());
        if(ObjectUtils.isEmpty(customerDao)){
            throw new BadRequestException(ExceptionConstants.INVALID_CUSTOMER);
        }
        inventoryRepository.findById(dto.getProductId()).orElseThrow(()->new BadRequestException(ExceptionConstants.INVALID_PRODUCT_ID));
    }




}
