package com.lingao.snkcomm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingao.snkcomm.model.entity.BmsTag;
import com.lingao.snkcomm.model.entity.BmsTopicTag;

import java.util.List;
import java.util.Set;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 16:00
 */
public interface IBmsTopicTagService extends IService<BmsTopicTag> {

    /**
     * 获取Topic Tag 关联记录
     *
     * @param topicId TopicId
     * @return
     */
    List<BmsTopicTag> selectByTopicId(String topicId);
    /**
     * 创建中间关系
     *
     * @param id
     * @param tags
     * @return
     */
    void createTopicTag(String id, List<BmsTag> tags);
    /**
     * 根据标签获取话题ID集合
     *
     * @param id
     * @return
     */
    Set<String> selectTopicIdsByTagId(String id);
}
