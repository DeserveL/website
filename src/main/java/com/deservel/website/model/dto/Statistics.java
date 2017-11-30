package com.deservel.website.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 后台统计对象
 *
 * @author DeserveL
 * @date 2017/11/30 16:56
 * @since 1.0.0
 */
@Data
public class Statistics implements Serializable {

    /**
     * 文章数
     */
    private long articles;

    /**
     * 页面数
     */
    private long pages;

    /**
     * 评论数
     */
    private long comments;

    /**
     * 分类数
     */
    private long categories;

    /**
     * 标签数
     */
    private long tags;

    /**
     * 附件数
     */
    private long attachs;

}
