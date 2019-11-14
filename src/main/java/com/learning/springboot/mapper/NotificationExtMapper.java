package com.learning.springboot.mapper;

import com.learning.springboot.dto.NotificationDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotificationExtMapper {

    List<NotificationDTO> listByUserId(@Param("userId") Long userId,
                                       @Param("offset") Integer offset,
                                       @Param("size") Integer size);

    int updateStatus(@Param("userId") Long userId);

}
