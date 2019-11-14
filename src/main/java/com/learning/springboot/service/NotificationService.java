package com.learning.springboot.service;

import com.learning.springboot.dto.NotificationDTO;
import com.learning.springboot.dto.PaginationDTO;
import com.learning.springboot.enums.NotificationStatusEnum;
import com.learning.springboot.mapper.NotificationExtMapper;
import com.learning.springboot.mapper.NotificationMapper;
import com.learning.springboot.model.Notification;
import com.learning.springboot.model.NotificationExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationExtMapper notificationExtMapper;

    /**
     * 获取消息通知分页信息
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Long userId, Integer page, Integer size){

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);

        paginationDTO.setPagination(totalCount, page, size);
        Integer offset = 5 * (paginationDTO.getPage() - 1);
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));

        if(notifications.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = notificationExtMapper.listByUserId(userId, offset, size);

        paginationDTO.setDataList(notificationDTOS);
        return paginationDTO;

    }

    /**
     * 统计未读消息
     * @param userId
     * @return
     */
    public Long countUnread(Long userId){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    /**
     * 更新已读状态
     * @param userId
     */
    public void read(Long userId){
        Integer result = notificationExtMapper.updateStatus(userId);
    }

}
