package com.learning.springboot.mapper;

import com.learning.springboot.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Long id);

    int incComment(Long id);

    List<Question> selectRelated(Question question);

    List<Question> hot();

}
