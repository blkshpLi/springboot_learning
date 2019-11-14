package com.learning.springboot.dto;

import lombok.Data;

@Data
public class FileDTO {
    private int success;
    private String message;
    private String url;
}
