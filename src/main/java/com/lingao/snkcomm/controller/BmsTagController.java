package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.BmsTag;
import com.lingao.snkcomm.model.vo.PostVO;
import com.lingao.snkcomm.service.IBmsPostService;
import com.lingao.snkcomm.service.impl.IBmsTagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingao.
 * @description
 * @date 2022/5/8 - 12:07
 */

@RestController
@RequestMapping("/tag")
public class BmsTagController {
    @Autowired
    private IBmsTagServiceImpl bmsTagService;
    @Autowired
    private IBmsPostService bmsPostService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ApiResult<Page<PostVO>> getTopicsByTag(
            @RequestParam("name") String tagName,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {

        LambdaQueryWrapper<BmsTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BmsTag::getName, tagName);
        BmsTag one = bmsTagService.getOne(wrapper);
        if(ObjectUtils.isEmpty(one)){
            ApiResult.failed("话题不存在，或已被管理员删除");
        }
        Page<PostVO> listByTag = bmsPostService.getListByTag(new Page<>(page, size), one.getId());

        return ApiResult.success(listByTag);
    }

    @RequestMapping(value = "/getOtherTag", method = RequestMethod.GET)
    public ApiResult<Page<BmsTag>> getOtherTag(@RequestParam("name") String tagName){
        // 其他热门标签
        Page<BmsTag> hotTags = bmsTagService.page(new Page<>(1, 10),
                new LambdaQueryWrapper<BmsTag>()
                        .notIn(BmsTag::getName, tagName)
                        .orderByDesc(BmsTag::getTopicCount));
        return ApiResult.success(hotTags);
    }
}
