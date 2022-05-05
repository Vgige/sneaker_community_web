package com.lingao.snkcomm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.vo.PostVO;

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
}
