package com.learning.springboot.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"查找的问题不存在或已删除"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    SYS_ERR(2003,"系统错误"),
    NO_LOGIN(2004,"该操作需先登录"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在或已删除"),
    COMMENT_IS_EMPTY(2007,"回复内容不能为空"),
    FILE_FAILED_UPLOAD(2010,"文件上传失败");

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
