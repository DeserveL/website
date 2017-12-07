package com.deservel.website.model.dto;

import com.deservel.website.common.utils.DateUtils;
import com.deservel.website.model.po.Logs;

/**
 * 日志表的action字段
 *
 * @author DeserveL
 * @date 2017/12/5 11:40
 * @since 1.0.0
 */
public interface LogActions {

    String LOGIN = "登录后台";

    String UP_PWD = "修改密码";

    String UP_INFO = "修改个人信息";

    String DEL_ATTACH = "删除附件";

    String DEL_ARTICLE = "删除文章";

    String DEL_PAGE = "删除页面";

    String SYS_BACKUP = "系统备份";

    String SYS_SETTING = "保存系统设置";

    String THEME_SETTING = "主题设置";

    String INIT_SITE = "初始化站点";

    String RELOAD_SYS = "重启系统";

    static Logs logInstance(String action, String data, Integer authorId, String ip) {
        Logs logs = new Logs();
        logs.setAction(action);
        logs.setData(data);
        logs.setAuthorId(authorId);
        logs.setIp(ip);
        logs.setCreated(DateUtils.getCurrentUnixTime());
        return logs;
    }
}
