package com.learning.springboot.controller;

import com.learning.springboot.dto.CommentDTO;
import com.learning.springboot.dto.ResultDTO;
import com.learning.springboot.mapper.CommentMapper;
import com.learning.springboot.model.Comment;
import com.learning.springboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @PostMapping
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if (user == null){
            return new ResultDTO(2002, "需先登录才能进行评论");
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setContent(commentDTO.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        commentMapper.insert(comment);
        return "success";
    }

}
