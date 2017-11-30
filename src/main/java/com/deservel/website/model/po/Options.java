package com.deservel.website.model.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 配置表
 *
 * @author DeserveL
 * @date 2017/11/30 14:50
 * @since 1.0.0
 */
@Data
public class Options {
    /**
     * 配置名称
     */
    @Id
    private String name;

    /**
     * 配置值
     */
    private String value;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 获取配置名称
     *
     * @return name - 配置名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置配置名称
     *
     * @param name 配置名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取配置值
     *
     * @return value - 配置值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置配置值
     *
     * @param value 配置值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取配置描述
     *
     * @return description - 配置描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置配置描述
     *
     * @param description 配置描述
     */
    public void setDescription(String description) {
        this.description = description;
    }
}