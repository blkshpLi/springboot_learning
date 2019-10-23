package com.learning.springboot.advice;

import com.learning.springboot.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {

    /**
     * 错误处理
     * @param request
     * @param e
     * @param model
     * @return
     */
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model){
        if (e instanceof CustomizeException) {
            model.addAttribute("message",e.getMessage());
        }else{
            model.addAttribute("message","失去与服务器的连接");
        }
        return new ModelAndView("error");
    }
}
