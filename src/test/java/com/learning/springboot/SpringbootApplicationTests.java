package com.learning.springboot;

import com.learning.springboot.mapper.QuestionMapper;
import com.learning.springboot.model.Question;
import com.learning.springboot.model.QuestionExample;
import com.learning.springboot.repository.QuestionRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

    @Autowired
    JestClient jestClient;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionMapper questionMapper;

    @Test
    public void search(){
        for(Question question : questionRepository.findByTitleLike("es")){
            System.out.println(question.getTitle());
        }
    }

    @Test
    public void contextLoads() {
        Question question = new Question();
        question.setId(1l);
        question.setTitle("测试es索引");
        question.setTag("elasticsearch");

        Index index = new Index.Builder(question).index("community").type("question").build();
        try{
            jestClient.execute(index);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void multiCreate() {
        List<Question> questions = questionMapper.selectByExample(new QuestionExample());
        if(questions.size() != 0){
            for(Question question : questions){
                Index index = new Index.Builder(question).index("community").type("question").build();
                try{
                    jestClient.execute(index);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void jestSearch(){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title","springboot"));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.fragmentSize(50);
        searchSourceBuilder.highlighter(highlightBuilder);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("community").addType("question").build();
        try{
            SearchResult searchResult = jestClient.execute(search);
            List<SearchResult.Hit<Question, Void>> hits = searchResult.getHits(Question.class);
            for(SearchResult.Hit<Question, Void> hit :hits){
                Question question = hit.source;
                Map<String, List<String>> highlight = hit.highlight;
                List<String> titles = highlight.get("title");
                if(titles.size() != 0){
                    question.setTitle(titles.get(0));
                }
                System.out.println("id："+ question.getId());
                System.out.println("标题："+ question.getTitle());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
