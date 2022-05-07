package com.lingao.snkcomm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingao.snkcomm.model.entity.BmsComment;
import com.lingao.snkcomm.model.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/7 - 16:33
 */
@Repository
public interface BmsCommentMapper extends BaseMapper<BmsComment> {

    /**
     * getCommentsByTopicID
     *
     * @param topicid
     * @return
     */
    List<CommentVO> getCommentsByTopicID(@Param("topicid") String topicid);
}
