package com.ecommerceservice.orders.model.request;

import com.ecommerceservice.utility.ExceptionConstants;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class AllOrdersRequestDto {
    private List<Integer> orderStatus;
}
