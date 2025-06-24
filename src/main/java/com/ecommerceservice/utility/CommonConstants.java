package com.ecommerceservice.utility;

import java.util.List;

public class CommonConstants {
    public static final String PROCESSING_STATUS="Processing";
    public static final String SUCCESS_STATUS="Success";
    public static final String FAILED_STATUS="Failed";
    public static final String PENDING_STATUS="Pending";
    public static final String DELIVERED_STATUS="Delivered";
    public static final String SHIPPED_STATUS="Shipped";

    public static final Integer PROCESSING_STATUS_ID=1;
    public static final Integer SUCCESS_STATUS_ID=2;
    public static final Integer FAILED_STATUS_ID=3;
    public static final Integer PENDING_STATUS_ID=4;
    public static final Integer DELIVERED_STATUS_ID=6;
    public static final Integer SHIPPED_STATUS_ID=5;


    public static final Integer ORDER_MODULE_ID=1;
    public static final Integer PAYMENT_MODULE_ID=2;
    public static final Integer SHIPPING_MODULE_ID=3;
    public static final Integer INVENTORY_MODULE_ID=4;

    public static final List<Integer> notificationTypeIds = List.of(1,2,3);

}
