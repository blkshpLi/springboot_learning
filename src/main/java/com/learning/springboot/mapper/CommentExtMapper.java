package com.learning.springboot.mapper;

public interface CommentExtMapper {
    int incReplyCount(Long id);
    int incLikeCount(Long id);
}
