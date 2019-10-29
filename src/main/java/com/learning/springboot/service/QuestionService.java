package com.learning.springboot.service;

import com.learning.springboot.dto.PaginationDTO;
import com.learning.springboot.dto.QuestionDTO;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.exception.CustomizeException;
import com.learning.springboot.mapper.QuestionExtMapper;
import com.learning.springboot.mapper.QuestionMapper;
import com.learning.springboot.mapper.UserMapper;
import com.learning.springboot.model.Question;
import com.learning.springboot.model.QuestionExample;
import com.learning.springboot.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 传递首页问题列表信息
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Integer page, Integer size){
        //创建分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount, page, size);
        Integer offset = 5 * (paginationDTO.getPage() - 1);

        //以分页形式获取提问信息
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            //User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    /**
     * 个人资料页面的提问列表信息
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO listByUserId(Long userId, Integer page, Integer size){
        //创建分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        paginationDTO.setPagination(totalCount, page, size);
        Integer offset = 5 * (paginationDTO.getPage() - 1);
        //根据userId以分页形式获取提问信息
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));
        //List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    /**
     * 创建或更新提问信息
     * @param question
     */
    public void createOrUpdate(Question question){
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.updateByPrimaryKeySelective(question);
        }
    }

    /**
     * 查看问题信息
     * @param id
     * @return
     */
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        //若查找失败则抛出异常
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        //User user = userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 记录阅览数
     * @param id
     */
    public void incView(Long id) {
        questionExtMapper.incView(id);
    }
}
