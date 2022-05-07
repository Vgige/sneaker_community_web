package com.lingao.snkcomm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.BmsTag;

import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 15:59
 */
public interface IBmsTagService extends IService<BmsTag> {

    /**
     * 获取标签关联话题
     *
     * @param topicPage
     * @param id
     * @return
     */
//    Page<BmsPost> selectTopicsByTagId(Page<BmsPost> topicPage, String id);

    List<BmsTag> selectBatchIds(List<String> tagIds);

    List<BmsTag> insertTags(List<String> tagNames);
}
