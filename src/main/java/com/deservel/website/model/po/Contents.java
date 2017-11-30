package com.deservel.website.model.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 内容
 *
 * @author DeserveL
 * @date 2017/11/30 14:48
 * @since 1.0.0
 */
@Data
public class Contents {
    /**
     * post表主键
     */
    @Id
    private Integer cid;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容缩略名
     */
    private String slug;

    /**
     * 内容生成时的GMT unix时间戳
     */
    private Integer created;

    /**
     * 内容更改时的GMT unix时间戳
     */
    private Integer modified;

    /**
     * 内容所属用户id
     */
    @Column(name = "author_id")
    private Integer authorId;

    /**
     * 内容类别
     */
    private String type;

    /**
     * 内容状态
     */
    private String status;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 分类列表
     */
    private String categories;

    /**
     * 点击次数
     */
    private Integer hits;

    /**
     * 内容所属评论数
     */
    @Column(name = "comments_num")
    private Integer commentsNum;

    /**
     * 是否允许评论
     */
    @Column(name = "allow_comment")
    private Boolean allowComment;

    /**
     * 是否允许ping
     */
    @Column(name = "allow_ping")
    private Boolean allowPing;

    /**
     * 允许出现在聚合中
     */
    @Column(name = "allow_feed")
    private Boolean allowFeed;

    /**
     * 内容文字
     */
    private String content;
}