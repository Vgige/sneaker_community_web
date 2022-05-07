package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.mapper.BmsTagMapper;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.BmsTag;
import com.lingao.snkcomm.service.IBmsPostService;
import com.lingao.snkcomm.service.IBmsTagService;
import com.lingao.snkcomm.service.IBmsTopicTagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 16:23
 */
@Service
public class IBmsTagServiceImpl extends ServiceImpl<BmsTagMapper, BmsTag> implements IBmsTagService {
//    @Override
//    public Page<BmsPost> selectTopicsByTagId(Page<BmsPost> topicPage, String id) {
//        // 获取关联的话题ID
//        Set<String> ids = IBmsTopicTagService.selectTopicIdsByTagId(id);
//        LambdaQueryWrapper<BmsPost> wrapper = new LambdaQueryWrapper<>();
//        wrapper.in(BmsPost::getId, ids);
//
//        return IBmsPostService.page(topicPage, wrapper);
//    }
    @Override
    public List<BmsTag> insertTags(List<String> tagNames) {
        List<BmsTag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            BmsTag tag = this.baseMapper.selectOne(new LambdaQueryWrapper<BmsTag>().eq(BmsTag::getName, tagName));
            if (tag == null) {
                tag = BmsTag.builder().name(tagName).build();
                this.baseMapper.insert(tag);
            } else {
                tag.setTopicCount(tag.getTopicCount() + 1);
                this.baseMapper.updateById(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }
    @Override
    public List<BmsTag> selectBatchIds(List<String> tagIds) {
        return baseMapper.selectBatchIds(tagIds);
    }
}
