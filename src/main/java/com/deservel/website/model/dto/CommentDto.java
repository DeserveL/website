package com.deservel.website.model.dto;

import com.deservel.website.model.po.Comments;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    public CommentDto(Comments comments) {
        setAuthor(comments.getAuthor());
        setMail(comments.getMail());
        setCoid(comments.getCoid());
        setAuthorId(comments.getAuthorId());
        setUrl(comments.getUrl());
        setCreated(comments.getCreated());
        setAgent(comments.getAgent());
        setIp(comments.getIp());
        setContent(comments.getContent());
        setOwnerId(comments.getOwnerId());
        setCid(comments.getCid());
        setParent(comments.getParent());
    }

}
