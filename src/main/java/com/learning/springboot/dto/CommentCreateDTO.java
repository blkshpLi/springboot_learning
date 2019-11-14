package com.learning.springboot.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {

    private Long parentId;

    private Integer type;

    private String content;

    private Long issueId;

}
