package com.ecommerceservice.notifications.service;

import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.dao.NotificationMessage;
import com.ecommerceservice.notifications.model.request.TemplatePlaceHoldersDto;
import com.ecommerceservice.notifications.repository.NotificationMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NotificationMessageServiceImpl {
    @Autowired
    private  NotificationMessageRepository notificationMessageRepository;

    public String generateMessage(Integer moduleId, Integer statusId, Integer notificationType,TemplatePlaceHoldersDto dto) {
        NotificationMessage notificationMessage = notificationMessageRepository
                .findByNotificationModuleIdAndStatusAndNotificationType(moduleId, statusId, notificationType);
        Map<String,String> placeHolders = buildPlaceholders(moduleId,dto);
        return replacePlaceholders(notificationMessage.getTemplate(), placeHolders);
    }

    private String replacePlaceholders(String template, Map<String, String> values) {
        String result = template;

        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            result = result.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
        }

        return result;
    }

    public  Map<String, String> buildPlaceholders(Integer moduleId, TemplatePlaceHoldersDto data) {
        Map<String, String> map = new HashMap<>();

        switch (moduleId) {
            case 1: // Order
                    map.put("customer", data.getCustomerName());
                    map.put("product", data.getProductName());
                break;

            case 2: // Payment
                    map.put("customer", data.getCustomerName());
                    map.put("product", data.getProductName());
                    map.put("amount", String.valueOf(data.getAmount()));
                break;

            case 3: // Shipping
                    map.put("customer", data.getCustomerName());
                    map.put("product", data.getProductName());
                break;
        }
        return map;
    }
}
