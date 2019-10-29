package com.learning.springboot.dto;

import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {
    private  Integer code;
    private String message;

    public ResultDTO(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode){
        return new ResultDTO(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e){
        return new ResultDTO(e.getCode(), e.getMessage());
    }

}
