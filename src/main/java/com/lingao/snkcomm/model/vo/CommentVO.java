package com.lingao.snkcomm.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lingao.
 * @description 评论vo
 * @date 2022/5/7 - 16:32
 */
@Data
public class CommentVO {

    private String id;

    private String content;

    private String topicId;

    private String userId;

    private String username;

    private String alias;

    private String avatar;

    private Date createTime;

}
