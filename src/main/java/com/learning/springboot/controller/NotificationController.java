package com.learning.springboot.controller;

import com.learning.springboot.dto.NotificationDTO;
import com.learning.springboot.dto.ResultDTO;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.mapper.NotificationExtMapper;
import com.learning.springboot.mapper.NotificationMapper;
import com.learning.springboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationExtMapper notificationExtMapper;

    @ResponseBody
    @GetMapping("/notification")
    public ResultDTO<List<NotificationDTO>> getMyNotification(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        List<NotificationDTO> notificationDTOS = notificationExtMapper.listByUserId(user.getId(),0,10);

        return ResultDTO.okOf(notificationDTOS);
    }

    @ResponseBody
    @GetMapping("/profile/replies/del/{id}")
    public ResultDTO del(@PathVariable("id") Long id,
                         HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        int i = notificationMapper.deleteByPrimaryKey(id);
        if(i == 1) {
            return ResultDTO.okOf();
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERR);
        }

    }

}
