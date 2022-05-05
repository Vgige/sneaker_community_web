package com.lingao.snkcomm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingao.snkcomm.model.entity.BmsTopicTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 15:49
 */
@Repository
public interface BmsTopicTagMapper extends BaseMapper<BmsTopicTag> {
    /**
     * 根据标签获取话题ID集合
     *
     * @param id
     * @return
     */
    Set<String> getTopicIdsByTagId(@Param("id") String id);
}
