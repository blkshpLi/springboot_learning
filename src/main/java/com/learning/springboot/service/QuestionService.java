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
import com.learning.springboot.util.ModelUtils;
import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    JestClient jestClient;

    /**
     * 传递首页问题列表信息
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Integer page, Integer size) {
        //创建分页对象
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount, page, size);
        Integer offset = 5 * (paginationDTO.getPage() - 1);

        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        //以分页形式获取提问信息
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));
        //List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            User user = (User) ModelUtils.convert(userMapper.selectByPrimaryKey(question.getCreator())
                    , new String[]{"id", "name", "avatarUrl"});
            //User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setDataList(questionDTOList);

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
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        questionExample.setOrderByClause("gmt_create desc");
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
        paginationDTO.setDataList(questionDTOList);

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
            questionMapper.insertSelective(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.updateByPrimaryKeySelective(question);
        }
        createIndex(question);
    }

    /**
     * 创建提问的索引
     * @param question
     */
    public void createIndex(Question question){
        Index index = new Index.Builder(question).index("community").type("question").build();
        try{
            jestClient.execute(index);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 删除es中目标提问的数据
     * @param id
     */
    public void deleteData(Long id){
        String targetId = String.valueOf(id);
        Delete delete = new Delete.Builder(targetId).index("community").type("question").build();
        try{
            jestClient.execute(delete);
        }catch (IOException e){
            e.printStackTrace();
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

    /**
     * 展示相关问题
     * @param questionDTO
     * @return
     */
    public List<Question> selectedRelated(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return null;
        }
        String tags = questionDTO.getTag();
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(tags.trim().replace(";","|"));
        List<Question> questions = questionExtMapper.selectRelated(question);
        return questions;
    }
}
