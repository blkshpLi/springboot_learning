package com.learning.springboot.repository;

import com.learning.springboot.model.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface QuestionRepository extends ElasticsearchRepository<Question,Long> {
    List<Question> findByTitleLike(String keyword);
}
