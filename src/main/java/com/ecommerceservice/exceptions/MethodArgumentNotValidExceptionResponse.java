package com.ecommerceservice.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodArgumentNotValidExceptionResponse {
    private Integer status;
    private List<String> messages;
}
