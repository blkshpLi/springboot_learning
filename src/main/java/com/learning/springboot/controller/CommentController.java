package com.learning.springboot.controller;

import com.learning.springboot.dto.CommentCreateDTO;
import com.learning.springboot.dto.ResultDTO;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.model.Comment;
import com.learning.springboot.model.User;
import com.learning.springboot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setCommentator(user.getId());
        comment.setType(commentCreateDTO.getType());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        commentService.insert(comment);
        return new ResultDTO(200,"请求成功");
    }

}
