package com.ecommerceservice.utility;

import com.ecommerceservice.customers.model.response.WishlistCartCountResponseDto;
import com.ecommerceservice.inventory.model.response.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
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

    public WishlistCartCountResponseDto getWishlistCartCountDetails(Long customerId){
        String query = """
                SELECT
                  (SELECT COUNT(*) FROM "CartItem" WHERE "CustomerID" = %d) AS "cartCount",
                  (SELECT COUNT(*) FROM "WishlistItem" WHERE "CustomerID" = %d) AS "wishlistCount";
                
                """.formatted(customerId,customerId);

        return  jdbcTemplate.queryForObject(query,wishlistCartCountResponseDtoRowMapper);
    }

    private final RowMapper<WishlistCartCountResponseDto> wishlistCartCountResponseDtoRowMapper = (rs, rowNum) -> {
      WishlistCartCountResponseDto dto = new WishlistCartCountResponseDto();
      dto.setCartCount(rs.getInt("cartCount"));
      dto.setWishlistCount(rs.getInt("wishlistCount"));
      return dto;
    };

}
