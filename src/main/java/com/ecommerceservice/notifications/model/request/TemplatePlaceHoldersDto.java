package com.ecommerceservice.notifications.model.request;

import lombok.Data;

@Data
public class TemplatePlaceHoldersDto {
    private String customerName;
    private String productName;
    private Double amount;
}
