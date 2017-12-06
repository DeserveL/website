package com.deservel.website.dao;

import com.deservel.website.common.mapper.MyMapper;
import com.deservel.website.model.po.Contents;
import org.apache.ibatis.annotations.Param;

/**
 * 内容表
 *
 * @author DeserveL
 * @date 2017/11/30 14:33
 * @since 1.0.0
 */
public interface ContentsMapper extends MyMapper<Contents> {

    /**
     * 根据类别和状态 查询内容条数
     *
     * @param type
     * @param status
     * @return
     */
    Long countContentsByTypeAndStatus(@Param("type") String type, @Param("status") String status);
}