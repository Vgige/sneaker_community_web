package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.mapper.BmsTopicMapper;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.BmsTag;
import com.lingao.snkcomm.model.entity.BmsTopicTag;
import com.lingao.snkcomm.model.vo.PostVO;
import com.lingao.snkcomm.service.IBmsPostService;
import com.lingao.snkcomm.service.IBmsTopicTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 16:21
 */
@Service
public class IBmsPostServiceImpl extends ServiceImpl<BmsTopicMapper, BmsPost> implements IBmsPostService {
    @Autowired
    private IBmsTopicTagService bmsTopicTagService;
    @Autowired
    private IBmsTagServiceImpl bmsTagService;
    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        // 查询话题
        Page<PostVO> iPage = baseMapper.selectListAndPage(page, tab);
        // 查询话题的标签
        this.setTopicTags(iPage);
        return iPage;
    }

    private void setTopicTags(Page<PostVO> iPage) {
        iPage.getRecords().forEach(topic -> {
            List<BmsTopicTag> topicTags = bmsTopicTagService.selectByTopicId(topic.getId());
            if (!topicTags.isEmpty()) {
                List<String> tagIds = topicTags.stream().map(BmsTopicTag::getTagId).collect(Collectors.toList());
                List<BmsTag> tags = bmsTagService.selectBatchIds(tagIds);
                topic.setTags(tags);
            }
        });
    }
}
