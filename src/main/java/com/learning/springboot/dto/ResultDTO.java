package com.learning.springboot.dto;

import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private  Integer code;
    private String message;
    private T data;

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

    public static ResultDTO okOf(){
        return new ResultDTO(200,"请求成功");
    }

    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDTO = new ResultDTO(200,"请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

}
