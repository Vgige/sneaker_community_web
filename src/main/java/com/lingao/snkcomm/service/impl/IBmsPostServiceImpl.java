package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.common.exception.ApiAsserts;
import com.lingao.snkcomm.common.exception.ApiException;
import com.lingao.snkcomm.mapper.BmsTopicMapper;
import com.lingao.snkcomm.model.dto.CreateTopicDTO;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.BmsTag;
import com.lingao.snkcomm.model.entity.BmsTopicTag;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.PostVO;
import com.lingao.snkcomm.model.vo.ProfileVO;
import com.lingao.snkcomm.service.IBmsPostService;
import com.lingao.snkcomm.service.IBmsTopicTagService;
import com.lingao.snkcomm.service.IUmsUserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
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
    @Autowired
    private IUmsUserService umsUserService;

    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        // 查询话题
        Page<PostVO> iPage = baseMapper.selectListAndPage(page, tab);
        // 查询话题的标签
        this.setTopicTags(iPage);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BmsPost create(CreateTopicDTO dto, UmsUser user) {
        BmsPost topic1 = this.baseMapper.selectOne(new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getTitle, dto.getTitle()));
        if(!ObjectUtils.isEmpty(topic1)){
            throw new ApiException("话题已存在，请修改！");
        }

        // 封装
        BmsPost topic = BmsPost.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .content(EmojiParser.parseToAliases(dto.getContent()))
                .createTime(new Date())
                .build();
        this.baseMapper.insert(topic);

        // 用户积分增加
//        int newScore = user.getScore() + 1;
//        umsUserMapper.updateById(user.setScore(newScore));

        // 标签
        if (!ObjectUtils.isEmpty(dto.getTags())) {
            // 保存标签
            List<BmsTag> tags = bmsTagService.insertTags(dto.getTags());
            // 处理标签与话题的关联
            bmsTopicTagService.createTopicTag(topic.getId(), tags);
        }

        return topic;
    }

    @Override
    public Map<String, Object> viewTopic(String id) {
        Map<String, Object> map = new HashMap<>(16);
        BmsPost topic = this.baseMapper.selectById(id);
        Assert.notNull(topic, "当前话题不存在,或已被作者删除");
        // 查询话题详情
        topic.setView(topic.getView() + 1);
        this.baseMapper.updateById(topic);
        // emoji转码
        topic.setContent(EmojiParser.parseToUnicode(topic.getContent()));
        map.put("topic", topic);
        // 标签
        QueryWrapper<BmsTopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmsTopicTag::getTopicId, topic.getId());
        Set<String> set = new HashSet<>();
        for (BmsTopicTag articleTag : bmsTopicTagService.list(wrapper)) {
            set.add(articleTag.getTagId());
        }
        List<BmsTag> tags = bmsTagService.listByIds(set);
        map.put("tags", tags);

        // 作者
        ProfileVO user = umsUserService.getUserProfile(topic.getUserId());
        map.put("user", user);

        return map;
    }

    @Override
    public List<BmsPost> getRecommend(String id) {
        return this.baseMapper.selectRecommend(id);
    }
//    @Override
//    public Page<PostVO> searchByKey(String keyword, Page<PostVO> page) {
//        // 查询话题
//        Page<PostVO> iPage = this.baseMapper.searchByKey(page, keyword);
//        // 查询话题的标签
//        setTopicTags(iPage);
//        return iPage;
//    }
//
//    private void setTopicTags(Page<PostVO> iPage) {
//        iPage.getRecords().forEach(topic -> {
//            List<BmsTopicTag> topicTags = IBmsTopicTagService.selectByTopicId(topic.getId());
//            if (!topicTags.isEmpty()) {
//                List<String> tagIds = topicTags.stream().map(BmsTopicTag::getTagId).collect(Collectors.toList());
//                List<BmsTag> tags = bmsTagMapper.selectBatchIds(tagIds);
//                topic.setTags(tags);
//            }
//        });
//    }

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
