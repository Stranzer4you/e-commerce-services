package com.ecommerceservice.orders.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDataResponseDTO {
    private Long orderId;
    private Double amountPaid;
    private String productName;
    private Long productId;
    private Integer status;
    private Double rating;
    private String orderedAt;
    private String statusName;
}
