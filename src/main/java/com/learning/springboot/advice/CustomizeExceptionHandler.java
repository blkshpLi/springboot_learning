package com.learning.springboot.advice;

import com.alibaba.fastjson.JSON;
import com.learning.springboot.dto.ResultDTO;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
    ModelAndView handle(HttpServletRequest request,
                        HttpServletResponse response,
                        Throwable e,
                        Model model){
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)){
            ResultDTO resultDTO;
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERR);
            }

            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch(IOException ioe) {

            }
            return null;
        }
        else{
            if (e instanceof CustomizeException) {
                model.addAttribute("message",e.getMessage());
            }else{
                model.addAttribute("message","失去与服务器的连接");
            }
            return new ModelAndView("error");
        }
    }
}
