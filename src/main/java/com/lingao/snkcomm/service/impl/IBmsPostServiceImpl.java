package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.common.exception.ApiAsserts;
import com.lingao.snkcomm.common.exception.ApiException;
import com.lingao.snkcomm.mapper.BmsTagMapper;
import com.lingao.snkcomm.mapper.BmsTopicMapper;
import com.lingao.snkcomm.mapper.UmsUserMapper;
import com.lingao.snkcomm.model.dto.CreateTopicDTO;
import com.lingao.snkcomm.model.entity.*;
import com.lingao.snkcomm.model.vo.PostVO;
import com.lingao.snkcomm.model.vo.ProfileVO;
import com.lingao.snkcomm.service.IBmsCommentService;
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
    @Autowired
    private UmsUserMapper umsUserMapper;
    @Autowired
    private IBmsCommentService bmsCommentService;
    @Autowired
    private BmsTagMapper bmsTagMapper;

    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        // ????????????
        Page<PostVO> iPage = baseMapper.selectListAndPage(page, tab);
        // ?????????????????????
        this.setTopicTags(iPage);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BmsPost create(CreateTopicDTO dto, UmsUser user) {
        BmsPost topic1 = this.baseMapper.selectOne(new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getTitle, dto.getTitle()));
        if(!ObjectUtils.isEmpty(topic1)){
            throw new ApiException("??????????????????????????????");
        }

        // ??????
        BmsPost topic = BmsPost.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .content(EmojiParser.parseToAliases(dto.getContent()))
                .createTime(new Date())
                .build();
        this.baseMapper.insert(topic);

        // ??????????????????
        int newScore = user.getScore() + 1;
        umsUserMapper.updateById(user.setScore(newScore));

        // ??????
        if (!ObjectUtils.isEmpty(dto.getTags())) {
            // ????????????
            List<BmsTag> tags = bmsTagService.insertTags(dto.getTags());
            // ??????????????????????????????
            bmsTopicTagService.createTopicTag(topic.getId(), tags);
        }

        return topic;
    }

    @Override
    public Map<String, Object> viewTopic(String id) {
        Map<String, Object> map = new HashMap<>(16);
        BmsPost topic = this.baseMapper.selectById(id);
        Assert.notNull(topic, "?????????????????????,?????????????????????");
        // ??????????????????
        topic.setView(topic.getView() + 1);
        this.baseMapper.updateById(topic);
        // emoji??????
        topic.setContent(EmojiParser.parseToUnicode(topic.getContent()));
        map.put("topic", topic);
        // ??????
        QueryWrapper<BmsTopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmsTopicTag::getTopicId, topic.getId());
        Set<String> set = new HashSet<>();
        for (BmsTopicTag articleTag : bmsTopicTagService.list(wrapper)) {
            set.add(articleTag.getTagId());
        }
        List<BmsTag> tags = bmsTagService.listByIds(set);
        map.put("tags", tags);

        // ??????
        ProfileVO user = umsUserService.getUserProfile(topic.getUserId());
        map.put("user", user);

        return map;
    }

    @Override
    public List<BmsPost> getRecommend(String id) {
        return this.baseMapper.selectRecommend(id);
    }

    @Override
    public Page<PostVO> searchByKey(String keyword, Page<PostVO> page) {
        // ????????????
        Page<PostVO> iPage = this.baseMapper.searchByKey(page, keyword);
        // ?????????????????????
        this.setTopicTags(iPage);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public void deletePost(UmsUser umsUser, String id) {
        BmsPost byId = this.getById(id);
        if(ObjectUtils.isEmpty(byId)){
            ApiAsserts.fail("?????????????????????????????????");
        }
        if(!byId.getUserId().equals(umsUser.getId())){
            ApiAsserts.fail("?????????????????????");
        }
        this.removeById(id);
        bmsCommentService.remove(new LambdaQueryWrapper<BmsComment>().eq(BmsComment::getTopicId, byId.getId()));
        List<BmsTopicTag> bmsTopicTags = bmsTopicTagService.selectByTopicId(byId.getId());
        bmsTopicTagService.remove(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getTopicId, byId.getId()));
        if(!ObjectUtils.isEmpty(bmsTopicTags)){
            List<String> tagIds = bmsTopicTags.stream().map(BmsTopicTag::getTagId).collect(Collectors.toList());
            for (String tagId : tagIds) {
                BmsTag bmsTag = bmsTagMapper.selectById(tagId);
                if(!ObjectUtils.isEmpty(bmsTag)){
                    if(bmsTag.getTopicCount()>0){
                        bmsTag.setTopicCount(bmsTag.getTopicCount()-1);
                        bmsTagMapper.updateById(bmsTag);
                    }

                }
            }
        }

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
    @Override
    public Page<PostVO> getListByTag(Page<PostVO> page, String id) {
        // ????????????
        Page<PostVO> iPage = baseMapper.selectListByTagName(page, id);
        // ?????????????????????
        this.setTopicTags(iPage);
        return iPage;
    }

}
