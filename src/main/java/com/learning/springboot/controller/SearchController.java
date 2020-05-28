package com.learning.springboot.controller;

import com.learning.springboot.dto.ResultDTO;
import com.learning.springboot.model.Question;
import com.learning.springboot.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam("keyword") String keyword){
        if(keyword == null || StringUtils.isBlank(keyword)){
            return "redirect:/";
        }else{
            List<Question> questions = searchService.jestSearch(keyword);
            List<Question> result = questions;
            model.addAttribute("result",result);
            model.addAttribute("keyword",keyword);
            return "search";
        }
    }

    @ResponseBody
    @GetMapping("/preSearch/{keyword}")
    public ResultDTO<List<Question>> preSearch(@PathVariable(name="keyword") String keyword){
        List<Question> result;
        if(keyword == null || StringUtils.isBlank(keyword)){
            return null;
        }else{
            result = searchService.preSearch(keyword);
            return ResultDTO.okOf(result);
        }
    }


}
