package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.mapper.BmsTopicTagMapper;
import com.lingao.snkcomm.model.entity.BmsTopicTag;
import com.lingao.snkcomm.service.IBmsTopicTagService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
