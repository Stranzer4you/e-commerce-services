package com.ecommerceservice.utility.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ModuleEnum {
    ORDERS(1,"Orders"),
    PAYMENTS(2,"Payments"),
    SHIPPING(3,"Shipping"),
    INVENTORY(4,"Inventory");

    public final Integer moduleId;
    public final String moduleName;

    public static ModuleEnum fromStatusId(Integer moduleId) {
        for (ModuleEnum status : values()) {
            if (Objects.equals(status.getModuleId(), moduleId)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + moduleId);
    }

}
