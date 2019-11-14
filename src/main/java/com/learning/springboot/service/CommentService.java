package com.learning.springboot.service;

import com.learning.springboot.dto.CommentDTO;
import com.learning.springboot.enums.CommentTypeEnum;
import com.learning.springboot.enums.NotificationStatusEnum;
import com.learning.springboot.enums.NotificationTypeEnum;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.exception.CustomizeException;
import com.learning.springboot.mapper.*;
import com.learning.springboot.model.*;
import com.learning.springboot.util.ModelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment){
        if (comment.getParentId() == null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Long parentId = comment.getParentId();
            Comment dbComment = commentMapper.selectByPrimaryKey(parentId);
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            commentExtMapper.incReplyCount(parentId);

            //创建通知并插入数据库
            notificationMapper.insert(createNotify(comment, dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT));
        }

        if (comment.getType() == CommentTypeEnum.QUESTION.getType()){
            //回复问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (dbQuestion == null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            questionExtMapper.incComment(comment.getParentId());

            //创建通知并插入数据库
            notificationMapper.insert(createNotify(comment, dbQuestion.getCreator(), NotificationTypeEnum.REPLY_QUESTION));
        }
    }

    /**
     * 创建新的通知
     * @param comment
     * @param receiver
     * @param notificationTypeEnum
     * @return
     */
    private Notification createNotify(Comment comment, Long receiver ,NotificationTypeEnum notificationTypeEnum){
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(comment.getCommentator());
        notification.setOuterId(comment.getIssueId());
        notification.setReceiver(receiver);
        notification.setType(notificationTypeEnum.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        return notification;
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type){
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("like_count desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size() == 0){
            return null;
        }
        //获取评论用户Id
        List<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).distinct().collect(Collectors.toList());

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(commentators);
        List<User> users = userMapper.selectByExample(userExample);

        //获取评论用户并转为Map
        Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(),user -> {
            User target = (User)ModelUtils.convert(user , new String[]{"id", "name", "avatarUrl"});
            return target;
        }));

        //将评论与用户关联转换为commentDTOs
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOs;
    }

}
