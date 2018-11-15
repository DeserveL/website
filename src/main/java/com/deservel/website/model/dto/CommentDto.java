package com.deservel.website.model.dto;

import com.deservel.website.model.po.Comments;
import lombok.*;

import java.util.List;

/**
 * 评论
 *
 * @author fan
 * @create 2018-11-12 9:38
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommentDto extends Comments {
    private int levels;
    private List<Comments> children;
}
