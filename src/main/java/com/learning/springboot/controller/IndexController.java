package com.learning.springboot.controller;

import com.learning.springboot.dto.PaginationDTO;
import com.learning.springboot.mapper.QuestionExtMapper;
import com.learning.springboot.model.Question;
import com.learning.springboot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 首页
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "5") Integer size){
        PaginationDTO pagination = questionService.list(page,size);
        List<Question> questions = questionExtMapper.hot();
        model.addAttribute("hot",questions);
        model.addAttribute("pagination",pagination);
        return "index";
    }

}
