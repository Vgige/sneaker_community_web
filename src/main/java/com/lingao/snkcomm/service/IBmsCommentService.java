package com.lingao.snkcomm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingao.snkcomm.model.dto.CommentDTO;
import com.lingao.snkcomm.model.entity.BmsComment;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.CommentVO;

import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/7 - 16:36
 */
public interface IBmsCommentService extends IService<BmsComment> {
    /**
     *
     *
     * @param topicid
     * @return {@link BmsComment}
     */
    List<CommentVO> getCommentsByTopicID(String topicid);

    BmsComment create(CommentDTO dto, UmsUser principal);
}
