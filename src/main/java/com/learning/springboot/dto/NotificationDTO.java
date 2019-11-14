package com.learning.springboot.dto;

import com.learning.springboot.model.Question;
import com.learning.springboot.model.User;
import lombok.Data;

@Data
public class NotificationDTO {

    private Long id;

    private Long gmtCreate;

    private Integer type;

    private Integer status;

    private User notifier;

    private Question question;

}
