package com.deservel.website.dao;

import com.deservel.website.common.mapper.MyMapper;
import com.deservel.website.model.dto.CommentDto;
import com.deservel.website.model.po.Comments;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author DeserveL
 * @date 2017/11/30 14:33
 * @since 1.0.0
 */
@Repository
public interface CommentsMapper extends MyMapper<Comments> {
    List<CommentDto> selectArticleCommentsById(@Param("uid")Integer uid);
}
