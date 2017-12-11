package com.deservel.website.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author DeserveL
 * @date 2017/12/11 15:52
 * @since 1.0.0
 */
@Data
public class ThemeDto implements Serializable {

    /**
     * 主题名称
     */
    private String name;

    /**
     * 是否有设置项
     */
    private boolean hasSetting;

    public ThemeDto(String name) {
        this.name = name;
    }

}
