package com.ecommerceservice.inventory.service;

import com.ecommerceservice.inventory.dao.ProductCategoryDao;
import com.ecommerceservice.inventory.model.request.ProductCategoryDto;
import com.ecommerceservice.inventory.repository.ProductCategoryRepository;
import com.ecommerceservice.orders.model.request.InventoryUpdateEvent;
import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.utility.BaseResponseUtility;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.dao.Product;
import com.ecommerceservice.inventory.mapper.InventoryMapper;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;
import com.ecommerceservice.inventory.model.request.UpdateProductRequestDto;
import com.ecommerceservice.inventory.model.response.ProductResponseDTO;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.utility.JdbcUtil;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecommerceservice.utility.constants.CommonConstants.ZERO_AVAILABLE_QUANTITY;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    @Override
    public BaseResponse getAllProducts(Integer categoryId,String searchText) {
        List<ProductResponseDTO> responseDTOS = jdbcUtil.fetchProductListing(1L,categoryId,searchText);
        return BaseResponseUtility.getBaseResponse(responseDTOS);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse addProducts(AddProductRequestDTO data) throws BadRequestException {
        Boolean isProductExists = inventoryRepository.existsByProductNameIgnoreCase(data.getProductName());
        if (Boolean.TRUE.equals(isProductExists)) {
            throw new BadRequestException(ExceptionConstants.PRODUCT_ALREADY_EXISTS);
        }
        Product product = inventoryMapper.addProductDtoToProductDao(data);
        product.setCreatedAt(LocalDateTime.now());
        product.setIsActive(Boolean.TRUE);
        product = inventoryRepository.save(product);
        return BaseResponseUtility.getBaseResponse(product);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse getByProductId(Long productId) throws BadRequestException {
        Product product = fetchProductDetailsById(productId);
        return BaseResponseUtility.getBaseResponse(product);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse updateProductById(Long productId, UpdateProductRequestDto updateProductRequestDto) throws BadRequestException {
        Product product = fetchProductDetailsById(productId);
        product.setProductName(updateProductRequestDto.getProductName());
        product.setPrice(updateProductRequestDto.getPrice());
        product.setIsActive(updateProductRequestDto.getIsActive());
        product.setQuantity(updateProductRequestDto.getQuantity());
        product.setIsAvailable(updateProductRequestDto.getIsAvailable());
        product = inventoryRepository.save(product);
        return BaseResponseUtility.getBaseResponse(product);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse updateProductStatus(Long productId, Boolean isActive) throws BadRequestException {
        Product product = inventoryRepository.findById(productId).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_PRODUCT_ID));
        product.setIsActive(isActive);
        product = inventoryRepository.save(product);
        return BaseResponseUtility.getBaseResponse(product);
    }


    public Product fetchProductDetailsById(Long productId) throws BadRequestException {
        Product product = inventoryRepository.findByIdAndIsActiveTrue(productId);
        if (ObjectUtils.isEmpty(product)) {
            throw new BadRequestException(ExceptionConstants.INVALID_PRODUCT_ID);
        }
        return product;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateProductQuantities(InventoryUpdateEvent inventoryUpdateEvent) throws BadRequestException {

        Map<Long,Integer> inventoryUpdateRequest = new HashMap<>();
        inventoryUpdateEvent.getOrdersDetailsDaoList().forEach(x->{
            inventoryUpdateRequest.put(x.getProductId(), x.getQuantity());
        });
        List<Long> productIds = new ArrayList<>(inventoryUpdateRequest.keySet());
        List<Product> products = inventoryRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new BadRequestException(ExceptionConstants.ONE_OR_MORE_PRODUCTS_ARE_INVALID);
        }
        for (Product product : products) {
            Long productId = product.getId();
            Integer requestedQuantity = inventoryUpdateRequest.get(productId);
            Integer currentQuantity = product.getQuantity();
            if (requestedQuantity > currentQuantity) {
                throw new BadRequestException(ExceptionConstants.PRODUCT_OUT_OF_STOCK);
            }
            Integer updatedQuantity = currentQuantity - requestedQuantity;
            product.setQuantity(updatedQuantity);
            if (ZERO_AVAILABLE_QUANTITY.equals(updatedQuantity)) {
                product.setIsAvailable(false);
            }
        }
        inventoryRepository.saveAll(products);
    }

    @Override
    public BaseResponse addCategory(List<ProductCategoryDto> dto) {
        List<ProductCategoryDao> productCategoryDaoList = new ArrayList<>();
        log.info("dto -> {}",dto);
        dto.forEach(x->{
            ProductCategoryDao dao = new ProductCategoryDao();
            dao.setIsActive(Boolean.TRUE);
            dao.setCreatedAt(LocalDateTime.now());
            dao.setProductName(x.getCategoryName());
            productCategoryDaoList.add(dao);
        });
        log.info("productCategoryDaoList -{}",productCategoryDaoList);
        productCategoryRepository.saveAll(productCategoryDaoList);
        return BaseResponseUtility.getBaseResponse(productCategoryDaoList);
    }

    @Override
    public BaseResponse getAllCategories() {
        List<ProductCategoryDao> productCategoryDaos = productCategoryRepository.findAllByOrderByIdAsc();
        return BaseResponseUtility.getBaseResponse(productCategoryDaos);
    }

    @Override
    public BaseResponse getProductsByCategoryId(Integer categoryId) {
        List<Product> products = inventoryRepository.findAllByProductCategoryId(categoryId);
        List<ProductResponseDTO> responseDTOS = inventoryMapper.convertProductDaoListToDtoList(products);
        return BaseResponseUtility.getBaseResponse(responseDTOS);
    }


}
