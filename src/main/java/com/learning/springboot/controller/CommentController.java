package com.learning.springboot.controller;

import com.learning.springboot.dto.CommentCreateDTO;
import com.learning.springboot.dto.CommentDTO;
import com.learning.springboot.dto.ResultDTO;
import com.learning.springboot.enums.CommentTypeEnum;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.model.Comment;
import com.learning.springboot.model.User;
import com.learning.springboot.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setCommentator(user.getId());
        comment.setType(commentCreateDTO.getType());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @GetMapping("/comment/{id}")
    public ResultDTO<List<CommentDTO>> getComments(@PathVariable(name = "id") Long id ) {
        List<CommentDTO> commentDTOs = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOs);
    }

}
