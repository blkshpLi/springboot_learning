package com.learning.springboot.controller;

import com.learning.springboot.dto.PaginationDTO;
import com.learning.springboot.model.User;
import com.learning.springboot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 个人资料
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    /**
     * 重定向“我的提问”
     * @return
     */
    @GetMapping("/profile")
    public String reProfile(){
        return "redirect:/profile/questions";
    }

    /**
     * 个人资料的各项功能
     * @param action
     * @param request
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          HttpServletRequest request,
                          Model model,
                          @RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "size",defaultValue = "5") Integer size){

        User user = (User) request.getSession().getAttribute("user");

        if(user == null){
            return "redirect:/";
        }

        if("questions".equals(action)){
            PaginationDTO pagination = questionService.listByUserId(user.getId(),page,size);
            model.addAttribute("pagination",pagination);
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
        } else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
