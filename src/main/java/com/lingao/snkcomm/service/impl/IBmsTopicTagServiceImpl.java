package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.mapper.BmsTopicTagMapper;
import com.lingao.snkcomm.model.entity.BmsTag;
import com.lingao.snkcomm.model.entity.BmsTopicTag;
import com.lingao.snkcomm.service.IBmsTopicTagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 16:25
 */
@Service
public class IBmsTopicTagServiceImpl extends ServiceImpl<BmsTopicTagMapper, BmsTopicTag> implements IBmsTopicTagService {
    @Override
    public List<BmsTopicTag> selectByTopicId(String topicId) {
        QueryWrapper<BmsTopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmsTopicTag::getTopicId, topicId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void createTopicTag(String id, List<BmsTag> tags) {
        // 先删除topicId对应的所有记录
        this.baseMapper.delete(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getTopicId, id));

        // 循环保存对应关联
        tags.forEach(tag -> {
            BmsTopicTag topicTag = new BmsTopicTag();
            topicTag.setTopicId(id);
            topicTag.setTagId(tag.getId());
            this.baseMapper.insert(topicTag);
        });
    }

    @Override
    public Set<String> selectTopicIdsByTagId(String id) {
        return this.baseMapper.getTopicIdsByTagId(id);
    }
}
