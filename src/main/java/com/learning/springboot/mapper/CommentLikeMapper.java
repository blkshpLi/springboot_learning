package com.learning.springboot.mapper;

import com.learning.springboot.model.CommentLike;
import com.learning.springboot.model.CommentLikeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CommentLikeMapper {
    long countByExample(CommentLikeExample example);

    int deleteByExample(CommentLikeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CommentLike record);

    int insertSelective(CommentLike record);

    List<CommentLike> selectByExampleWithRowbounds(CommentLikeExample example, RowBounds rowBounds);

    List<CommentLike> selectByExample(CommentLikeExample example);

    CommentLike selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CommentLike record, @Param("example") CommentLikeExample example);

    int updateByExample(@Param("record") CommentLike record, @Param("example") CommentLikeExample example);

    int updateByPrimaryKeySelective(CommentLike record);

    int updateByPrimaryKey(CommentLike record);
}