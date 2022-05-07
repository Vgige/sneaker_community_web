package com.lingao.snkcomm.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lingao.
 * @description 创建帖子
 * @date 2022/5/6 - 12:24
 */
@Data
public class CreateTopicDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390852L;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签
     */
    private List<String> tags;

}
