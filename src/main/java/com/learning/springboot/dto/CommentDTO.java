package com.learning.springboot.dto;

import com.learning.springboot.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;

    private Long parentId;

    private Integer type;

    private Long commentator;

    private Long gmtCreate;

    private Long gmtModified;

    private Integer likeCount;

    private String content;

    private Integer replyCount;

    private Long issueId;

    private User user;

    private User replyTo;

    private boolean agreed;

}
