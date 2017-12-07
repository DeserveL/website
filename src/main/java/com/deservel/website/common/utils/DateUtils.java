package com.deservel.website.common.utils;

import java.util.Date;

/**
 * @author DeserveL
 * @date 2017/12/7 11:53
 * @since 1.0.0
 */
public interface DateUtils {

    static int getCurrentUnixTime() {
        return getUnixTimeByDate(new Date());
    }

    static int getUnixTimeByDate(Date date) {
        return (int) (date.getTime() / 1000L);
    }
}
