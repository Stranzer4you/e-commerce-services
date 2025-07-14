package com.ecommerceservice.notifications.model.request;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AllNotificationsRequestDto {
    private Long customerId;
    private Long moduleId;
    private Long notificationTypeId;
}
