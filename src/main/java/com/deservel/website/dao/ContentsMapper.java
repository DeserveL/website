package com.deservel.website.dao;

import com.deservel.website.common.mapper.MyMapper;
import com.deservel.website.model.po.Contents;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 内容表
 *
 * @author DeserveL
 * @date 2017/11/30 14:33
 * @since 1.0.0
 */
@Repository
public interface ContentsMapper extends MyMapper<Contents> {

    /**
     * 根据类别和状态 查询内容条数
     *
     * @param type
     * @param status
     * @return
     */
    Long countContentsByTypeAndStatus(@Param("type") String type, @Param("status") String status);

    /**
     * 获取随机文章
     *
     * @param type
     * @param status
     * @param limit
     * @return
     */
    @Select("select * from contents where type = #{type} and status = #{status} order by rand() * cid limit #{limit}")
    @ResultMap("BaseResultMap")
    List<Contents> selectRandomArticle(@Param("type") String type, @Param("status") String status, @Param("limit") Integer limit);

    /**
     * 获取文章
     *
     * @param mid
     * @param status
     * @param type
     * @return
     */
    @Select("select a.* from contents a left join relationships b on a.cid = b.cid " +
            "where b.mid = #{mid} and a.status = #{status} and a.type = #{type} order by a.created desc")
    @ResultMap("BaseResultMap")
    List<Contents> selectArticles(@Param("mid") Integer mid, @Param("status") String status, @Param("type") String type);

    /*     add times method     */

    /**
     * 增加一个阅读次数
     *
     * @param cid
     * @return
     */
    int updateReadTimes(Integer cid);

    /**
     * 增加一个评论次数
     *
     * @param cid
     * @return
     */
    int updateCommentTimes(Integer cid);
}
