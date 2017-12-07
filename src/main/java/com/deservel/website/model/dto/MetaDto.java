package com.deservel.website.model.dto;

import com.deservel.website.model.po.Metas;
import lombok.Data;

/**
 * 添加文章数
 *
 * @author DeserveL
 * @date 2017/12/07 23:56
 * @since 1.0.0
 */
@Data
public class MetaDto extends Metas {
    private int count;
}
