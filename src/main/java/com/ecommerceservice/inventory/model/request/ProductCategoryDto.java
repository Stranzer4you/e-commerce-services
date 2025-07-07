package com.ecommerceservice.inventory.model.request;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class ProductCategoryDto {
    @NotEmpty(message = ExceptionConstants.CATEGORY_NAME_SHOULD_NOT_BE_EMPTY)
    private String categoryName;
}
