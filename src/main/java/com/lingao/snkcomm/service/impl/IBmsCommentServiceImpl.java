package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.mapper.BmsCommentMapper;
import com.lingao.snkcomm.mapper.BmsTopicMapper;
import com.lingao.snkcomm.model.dto.CommentDTO;
import com.lingao.snkcomm.model.entity.BmsComment;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.CommentVO;
import com.lingao.snkcomm.service.IBmsCommentService;
import com.lingao.snkcomm.service.IBmsPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/7 - 16:37
 */
@Slf4j
@Service
public class IBmsCommentServiceImpl extends ServiceImpl<BmsCommentMapper, BmsComment> implements IBmsCommentService {
    @Autowired
    private BmsTopicMapper bmsTopicMapper;
    @Override
    public List<CommentVO> getCommentsByTopicID(String topicid) {
        List<CommentVO> lstBmsComment = new ArrayList<CommentVO>();
        try {
            lstBmsComment = this.baseMapper.getCommentsByTopicID(topicid);
        } catch (Exception e) {
            log.info("lstBmsComment失败");
        }
        return lstBmsComment;
    }

    @Override
    public BmsComment create(CommentDTO dto, UmsUser user) {
        BmsComment comment = BmsComment.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .topicId(dto.getTopic_id())
                .createTime(new Date())
                .build();
        this.baseMapper.insert(comment);
        BmsPost topic = bmsTopicMapper.selectById(dto.getTopic_id());
        //评论条数+1
        topic.setComments(topic.getComments() + 1);
        bmsTopicMapper.updateById(topic);
        return comment;
    }
}
