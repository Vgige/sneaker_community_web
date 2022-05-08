package com.lingao.snkcomm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingao.snkcomm.model.dto.CreateTopicDTO;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.PostVO;

import java.util.List;
import java.util.Map;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 16:01
 */
public interface IBmsPostService extends IService<BmsPost>{
    /**
     * 获取首页话题列表
     *
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> getList(Page<PostVO> page, String tab);
    /**
     * 发布
     *
     * @param dto
     * @param principal
     * @return
     */
    BmsPost create(CreateTopicDTO dto, UmsUser principal);
    /**
     * 查看话题详情
     *
     * @param id
     * @return
     */
    Map<String, Object> viewTopic(String id);
    /**
     * 获取随机推荐10篇
     *
     * @param id
     * @return
     */
    List<BmsPost> getRecommend(String id);
    /**
     * 根据标签名查询帖子
     *
     * @param page
     * @param id
     * @return
     */
    Page<PostVO> getListByTag(Page<PostVO> page, String id);
    /**
     * 关键字检索
     *
     * @param keyword
     * @param page
     * @return
     */
    Page<PostVO> searchByKey(String keyword, Page<PostVO> page);
    /**
     * 删除帖子
     *
     */
    void deletePost(UmsUser umsUser, String id);
}
