package com.learning.springboot.service;

import com.learning.springboot.dto.PaginationDTO;
import com.learning.springboot.dto.QuestionDTO;
import com.learning.springboot.mapper.QuestionMapper;
import com.learning.springboot.mapper.UserMapper;
import com.learning.springboot.model.Question;
import com.learning.springboot.model.User;
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
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size){
        //创建分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(questionMapper.count(), page, size);
        Integer offset = 5 * (paginationDTO.getPage() - 1);

        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO listByUserId(Integer userId, Integer page, Integer size){
        //创建分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(questionMapper.countByUserId(userId), page, size);
        Integer offset = 5 * (paginationDTO.getPage() - 1);

        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public void createOrUpdate(Question question){
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        User user = userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }
}
