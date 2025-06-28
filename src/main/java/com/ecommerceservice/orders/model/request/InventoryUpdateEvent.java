package com.ecommerceservice.orders.model.request;

import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import lombok.Data;

import java.util.List;

@Data
public class InventoryUpdateEvent {
    private List<OrdersDetailsDao> ordersDetailsDaoList;
}
