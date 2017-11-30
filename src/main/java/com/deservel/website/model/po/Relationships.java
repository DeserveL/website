package com.deservel.website.model.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 对应关系
 *
 * @author DeserveL
 * @date 2017/11/30 14:51
 * @since 1.0.0
 */
@Data
public class Relationships {
    /**
     * 内容主键
     */
    @Id
    private Integer cid;

    /**
     * 项目主键
     */
    @Id
    private Integer mid;
}