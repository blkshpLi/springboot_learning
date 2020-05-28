package com.learning.springboot.controller;

import com.learning.springboot.dto.NotificationDTO;
import com.learning.springboot.dto.PaginationDTO;
import com.learning.springboot.dto.ResultDTO;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.mapper.NotificationExtMapper;
import com.learning.springboot.mapper.QuestionMapper;
import com.learning.springboot.model.Question;
import com.learning.springboot.model.User;
import com.learning.springboot.service.NotificationService;
import com.learning.springboot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 个人资料
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

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
            PaginationDTO pagination = notificationService.list(user.getId(),page,size);
            //notificationService.read(user.getId());
            model.addAttribute("pagination",pagination);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","回复我的");
        }
        return "profile";
    }

    @ResponseBody
    @PostMapping("/profile/questions/remove")
    public ResultDTO removeQuestion(@RequestParam(value = "id") Long id,
                                    HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        if (question.getCreator() != user.getId()) {
            return new ResultDTO(3,"您并非该问题的发起者");
        }
        questionMapper.deleteByPrimaryKey(id);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @PostMapping("/read")
    public ResultDTO read(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        notificationService.read(user.getId());
        return ResultDTO.okOf();
    }

}
