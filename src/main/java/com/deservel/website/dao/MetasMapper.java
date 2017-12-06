package com.deservel.website.dao;

import com.deservel.website.common.mapper.MyMapper;
import com.deservel.website.model.po.Metas;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author DeserveL
 * @date 2017/11/30 14:34
 * @since 1.0.0
 */
public interface MetasMapper extends MyMapper<Metas> {

    /**
     * 根据类别获取
     *
     * @param type
     * @return
     */
    List<Metas> selectByType(@Param("type") String type);
}