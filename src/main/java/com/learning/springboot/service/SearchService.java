package com.learning.springboot.service;

import com.learning.springboot.model.Question;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    JestClient jestClient;

    /**
     * elasticsearch查询
     */
    public List<Question> jestSearch(String keyword){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title",keyword.trim()));
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
            List<Question> result = new ArrayList<>();
            for(SearchResult.Hit<Question, Void> hit :hits){
                Question question = hit.source;
                Map<String, List<String>> highlight = hit.highlight;
                List<String> titles = highlight.get("title");
                if(titles.size() != 0){
                    question.setTitle(titles.get(0));
                }
                result.add(question);
            }
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 预查找
     * @param keyword
     * @return
     */
    public List<Question> preSearch(String keyword){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title",keyword.trim()));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("community").addType("question").build();
        try{
            SearchResult searchResult = jestClient.execute(search);
            List<SearchResult.Hit<Question, Void>> hits = searchResult.getHits(Question.class);
            List<Question> result = new ArrayList<>();
            for(SearchResult.Hit<Question, Void> hit :hits){
                Question question = hit.source;
                result.add(question);
            }
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
