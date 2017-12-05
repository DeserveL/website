package com.deservel.website.model.dto;

import com.deservel.website.model.po.Logs;

/**
 * 日志表的action字段
 *
 * @author DeserveL
 * @date 2017/12/5 11:40
 * @since 1.0.0
 */
public enum LogActions {

    LOGIN("登录后台"), UP_PWD("修改密码"), UP_INFO("修改个人信息"),
    DEL_ARTICLE("删除文章"), DEL_PAGE("删除页面"), SYS_BACKUP("系统备份"),
    SYS_SETTING("保存系统设置"), INIT_SITE("初始化站点");

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    LogActions(String action) {
        this.action = action;
    }

    public Logs logInstance(String data, Integer authorId, String ip) {
        Logs logs = new Logs();
        logs.setAction(this.action);
        logs.setData(data);
        logs.setAuthorId(authorId);
        logs.setIp(ip);
        logs.setCreated((int) (System.currentTimeMillis() / 1000L));
        return logs;
    }
}
