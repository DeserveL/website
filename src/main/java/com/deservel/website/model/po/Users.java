package com.deservel.website.model.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户表
 *
 * @author DeserveL
 * @date 2017/11/30 14:51
 * @since 1.0.0
 */
@Data
public class Users {
    /**
     * user表主键
     */
    @Id
    private Integer uid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户的邮箱
     */
    private String email;

    /**
     * 用户的主页
     */
    @Column(name = "home_url")
    private String homeUrl;

    /**
     * 用户显示的名称
     */
    @Column(name = "screen_name")
    private String screenName;

    /**
     * 用户注册时的GMT unix时间戳
     */
    private Integer created;

    /**
     * 最后活动时间
     */
    private Integer activated;

    /**
     * 上次登录最后活跃时间
     */
    private Integer logged;

    /**
     * 用户组
     */
    @Column(name = "group_name")
    private String groupName;
}