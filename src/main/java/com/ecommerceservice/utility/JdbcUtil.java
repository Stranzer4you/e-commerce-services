package com.ecommerceservice.utility;

import com.ecommerceservice.customers.model.response.CartResponseDTO;
import com.ecommerceservice.customers.model.response.WishlistCartCountResponseDto;
import com.ecommerceservice.customers.model.response.WishlistResponseDTO;
import com.ecommerceservice.inventory.model.response.ProductResponseDTO;
import com.ecommerceservice.notifications.model.response.NotificationResponseDTO;
import com.ecommerceservice.orders.model.response.OrdersDataResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JdbcUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProductResponseDTO> fetchProductListing (Long customerId,Integer productCategoryId,String searchText){
        String query = """
                SELECT
                "p"."ID" as "id",
                "p"."ProductName" as "productName",
                "p"."Price" as "price",
                "p"."Quantity" as "quantity",
                "p"."IsAvailable" as "isAvailable",
                "p"."ProductCategoryID" as "productCategoryId",
                "p"."Rating" as "rating",
                CASE
                WHEN w."ProductID" IS NOT NULL THEN true
                ELSE false
                END AS "isWishListed",
                case
                when c."ProductID" IS NOT NULL THEN TRUE
                ELSE FALSE
                END AS "isInCart"
                FROM "Products" p
                LEFT JOIN "WishlistItem" w
                ON p."ID" = w."ProductID" AND w."CustomerID" = %d
                left join "CartItem" "c"
                on "c"."ProductID" = "p"."ID" AND "c"."CustomerID"=%d
                where "p"."IsActive"=true
                """.formatted(customerId,customerId);
        if(!ObjectUtils.isEmpty(productCategoryId)){
            query+= """
                    and "p"."ProductCategoryID"=%d
                    """.formatted(productCategoryId);
        }
        if(!ObjectUtils.isEmpty(searchText)){
            query+= """
                    and "p"."ProductName" ilike '%s'
                    """.formatted("%"+searchText+"%");
        }
        return  jdbcTemplate.query(query,productResponseDTORowMapper);
    }

    public WishlistCartCountResponseDto getWishlistCartCountDetails(Long customerId){
        String query = """
                SELECT
                  (SELECT COUNT(*) FROM "CartItem" WHERE "CustomerID" = %d) AS "cartCount",
                  (SELECT COUNT(*) FROM "WishlistItem" WHERE "CustomerID" = %d) AS "wishlistCount";
                
                """.formatted(customerId,customerId);

        return  jdbcTemplate.queryForObject(query,wishlistCartCountResponseDtoRowMapper);
    }

    public List<WishlistResponseDTO> getCustomerWishlistDetails(Long customer){
        String query = """
                select
                "p"."ID" as "productId",
                "p"."ProductName" as "productName",
                "p"."Price" as "price",
                "p"."Rating" as "rating",
                0 as "quantity"
                from "WishlistItem" "w"
                inner join "Products" "p"
                on "w"."ProductID"= "p"."ID" AND "p"."IsActive"=true
                where "w"."CustomerID"=%d
                order by "w"."CreatedAt" desc
                """.formatted(customer);
          return jdbcTemplate.query(query,wishlistResponseDTORowMapper);
    }

    public List<CartResponseDTO> getCustomerCartDetails(Long customerId) {
        String query = """
                select
                "c"."ID" AS "id",
                "p"."ID" as "productId",
                "p"."ProductName" as "productName",
                "c"."Price" as "price",
                "p"."Rating" as "rating",
                 "c"."Quantity" as "quantity"
                from "CartItem" "c"
                inner join "Products" "p"
                on "c"."ProductID"= "p"."ID" AND "p"."IsActive"=true
                where "c"."CustomerID"=%d
                order by "c"."CreatedAt" desc
                """.formatted(customerId);
        return  jdbcTemplate.query(query,cartResponseDTORowMapper);

    }

    public List<OrdersDataResponseDTO> getCustomerOrders(Long customerId, Integer statusId){
        String query = """
                select
                "orders"."OrderID" as "orderId",
                "orders"."AmountPaid"  as "amountPaid",
                "products"."ProductName" as "productName",
                "products"."ID"  as "productId",
                "o"."Status" as "status",
                "products"."Rating" as "rating",
                TO_CHAR("o"."CreatedAt", 'on FMMonth DD at HH:MIam') as "orderedAt",
                "status"."Status" as "statusName"
                from "OrdersDetails" as "orders"
                inner join "Products" "products"
                on "products"."ID"= "orders"."ProductID"
                INNER JOIN "Orders"  "o"
                on "o"."ID"= "orders"."OrderID"
                inner join "Status" "status" on "status"."ID"= "o"."Status"
                where "o"."CustomerID"=%d
                """.formatted(customerId);
        if(!ObjectUtils.isEmpty(statusId)){
            query+= """
                     and "o"."Status" =%d
                    """.formatted(statusId);
        }
        query+= """
                order by "o"."ID" desc
                """;
        return  jdbcTemplate.query(query,ordersDataResponseDTORowMapper);
    }

    public List<NotificationResponseDTO> getCustomerNotifications(Long customerId,Long moduleId,Long notificationTypeId){
       String query = """
               select
               "notification"."ID" AS "notificationId",
               "module"."Module" as "module",
               "type"."Type" as "notificationType",
               "notification"."Message" as "message",
               "status"."Status" as "status",
               "notification"."NotificationModuleID"  as "moduleId",
               "notification"."NotificationType" as "notificationTypeId"
               from "Notification" as "notification"
               inner join "Status" "status" on "status"."ID" = "notification"."Status"
               inner join "NotificationType" "type" on "type"."ID" = "notification"."NotificationType"
               inner join "NotificationModule" "module" on "module"."ID" = "notification"."NotificationModuleID"
               where "notification"."CustomerID"=%d
               """.formatted(customerId);
       if (!ObjectUtils.isEmpty(notificationTypeId)){
           query+= """
                   and "notification"."NotificationType" = %d
                   """.formatted(notificationTypeId);

       }
       if(!ObjectUtils.isEmpty(moduleId)){
           query+= """
                  AND "notification"."NotificationModuleID" = %d
                  """.formatted(moduleId);

       }
       query+= """
               order by "notification"."ID" desc
               """;
       return  jdbcTemplate.query(query,notificationResponseDTORowMapper);
    }

    private final RowMapper<NotificationResponseDTO> notificationResponseDTORowMapper = (rs, rowNum) -> {
      NotificationResponseDTO dto = new NotificationResponseDTO();
      dto.setMessage(rs.getString("message"));
      dto.setModule(rs.getString("module"));
      dto.setStatus(rs.getString("status"));
      dto.setNotificationType(rs.getString("notificationType"));
      dto.setNotificationTypeId(rs.getLong("notificationTypeId"));
      dto.setModuleId(rs.getLong("moduleId"));
      dto.setNotificationId(rs.getLong("notificationId"));
      return  dto;
    };

    private final RowMapper<ProductResponseDTO> productResponseDTORowMapper = (rs, rowNum) -> {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(rs.getLong("id"));
        dto.setProductName(rs.getString("productName"));
        dto.setPrice(rs.getDouble("price"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setIsAvailable(rs.getBoolean("isAvailable"));
        dto.setProductCategoryId(rs.getInt("productCategoryId"));
        dto.setRating(rs.getDouble("rating"));
        dto.setIsWishListed(rs.getBoolean("isWishListed"));
        dto.setIsInCart(rs.getBoolean("isInCart"));
        return dto;
    };


    private final RowMapper<WishlistCartCountResponseDto> wishlistCartCountResponseDtoRowMapper = (rs, rowNum) -> {
      WishlistCartCountResponseDto dto = new WishlistCartCountResponseDto();
      dto.setCartCount(rs.getInt("cartCount"));
      dto.setWishlistCount(rs.getInt("wishlistCount"));
      return dto;
    };

    private final RowMapper<WishlistResponseDTO> wishlistResponseDTORowMapper = (rs, rowNum) -> {
        WishlistResponseDTO dto = new WishlistResponseDTO();
        dto.setPrice(rs.getDouble("price"));
        dto.setRating(rs.getDouble("rating"));
        dto.setProductName(rs.getString("productName"));
        dto.setProductId(rs.getLong("productId"));
        return dto;
    };

    private final RowMapper<CartResponseDTO> cartResponseDTORowMapper = (rs, rowNum) -> {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setPrice(rs.getDouble("price"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setProductName(rs.getString("productName"));
        dto.setRating(rs.getDouble("rating"));
        dto.setProductId(rs.getLong("productId"));
        dto.setId(rs.getLong("id"));
        return  dto;
    };

    private final RowMapper<OrdersDataResponseDTO> ordersDataResponseDTORowMapper = (rs, rowNum) ->{
      OrdersDataResponseDTO dto  = new OrdersDataResponseDTO();
      dto.setStatus(rs.getInt("status"));
      dto.setOrderId(rs.getLong("orderId"));
      dto.setAmountPaid(rs.getDouble("amountPaid"));
      dto.setProductName(rs.getString("productName"));
      dto.setProductId(rs.getLong("productId"));
      dto.setRating(rs.getDouble("rating"));
      dto.setOrderedAt(rs.getString("orderedAt"));
      dto.setStatusName(rs.getString("statusName"));
      return dto;
    };

}
