package com.deservel.website.model.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 标签 分类 其他
 *
 * @author DeserveL
 * @date 2017/11/30 14:50
 * @since 1.0.0
 */
@Data
public class Metas {
    /**
     * 项目主键
     */
    @Id
    private Integer mid;

    /**
     * 名称
     */
    private String name;

    /**
     * 项目缩略名
     */
    private String slug;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 选项描述
     */
    private String description;

    /**
     * 项目排序
     */
    private Integer sort;

    /**
     * 父项目
     */
    private Integer parent;
}