package com.ecommerceservice.config;

import lombok.Data;

@Data
public class BaseResponseUtility {

    public static Result getResult(){
        Result result = new Result();
        result.setResponseCode(0);
        result.setResponseDescription("Success");
        return result;
    }
    public static BaseResponse getBaseResponse(Object data){
        BaseResponse response = new BaseResponse();
        response.setData(data);
        response.setStatus(200);
        response.setResult(getResult());
        return  response;
    }

    public static BaseResponse getBaseResponse(){
        BaseResponse response = new BaseResponse();
        response.setStatus(200);
        response.setResult(getResult());
        return  response;

    }
}
