package com.deservel.website.model.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 附件表
 *
 * @author DeserveL
 * @date 2017/11/30 14:47
 * @since 1.0.0
 */
@Data
public class Attach {
    /**
     * 附件主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 附件名称
     */
    private String fname;

    /**
     * 附件类型
     */
    private String ftype;

    /**
     * 附件key
     */
    private String fkey;

    /**
     * 创建时间
     */
    @Column(name = "author_id")
    private Integer authorId;

    /**
     * 创建时间
     */
    private Integer created;
}