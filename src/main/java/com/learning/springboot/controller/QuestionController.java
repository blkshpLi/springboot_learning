package com.learning.springboot.controller;

import com.learning.springboot.dto.CommentDTO;
import com.learning.springboot.dto.QuestionDTO;
import com.learning.springboot.enums.CommentTypeEnum;
import com.learning.springboot.service.CommentService;
import com.learning.springboot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 阅览提问
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Long id,
                           Model model,
                           HttpServletRequest request){
        //判断当前阅览的问题id是否与上一个重复，若不重复则增加阅读数
        Object obj = request.getSession().getAttribute("questionId");
        if(null == obj){
            //增加阅读数
            questionService.incView(id);
        }else{
            Long l = (Long)obj;
            if(l != id){
                questionService.incView(id);
            }
        }
        request.getSession().setAttribute("questionId",id);

        //获取问题信息
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOs = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOs);
        return "question";
    }



}
