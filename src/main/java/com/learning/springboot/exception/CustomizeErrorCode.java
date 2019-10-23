package com.learning.springboot.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"查找的问题不存在或已删除"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复");

    private Integer code;
    private String message;

    @Override
    public String getMessage() { return message; }

    @Override
    public Integer getCode() { return code; }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
