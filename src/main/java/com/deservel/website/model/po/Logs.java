package com.deservel.website.model.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 日志
 *
 * @author DeserveL
 * @date 2017/11/30 14:49
 * @since 1.0.0
 */
@Data
public class Logs {
    /**
     * 日志主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 产生的动作
     */
    private String action;

    /**
     * 产生的数据
     */
    private String data;

    /**
     * 发生人id
     */
    @Column(name = "author_id")
    private Integer authorId;

    /**
     * 日志产生的ip
     */
    private String ip;

    /**
     * 日志创建时间
     */
    private Integer created;
}