package com.lingao.snkcomm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author lingao.
 * @description 邮件信息DTO
 * @date 2022/5/10 - 10:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    /**
     * 发送邮箱列表
     */
    @NotEmpty
    private List<String> tos;

    /**
     * 主题
     */
    @NotBlank
    private String subject;

    /**
     * 内容
     */
    @NotBlank
    private String content;
}
