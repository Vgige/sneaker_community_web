package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.entity.BmsBillboard;
import com.lingao.snkcomm.model.entity.BmsTip;
import com.lingao.snkcomm.service.IBmsBillboardService;
import com.lingao.snkcomm.service.IBmsTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/2 - 17:31
 */
@RestController
@RequestMapping("/tip")
public class BmsTipController extends BaseController{
    @Autowired
    private IBmsTipService bmsTipService;

    @RequestMapping(value = "/today", method = RequestMethod.GET)
    public ApiResult<BmsTip> getRandomTip(){
        BmsTip tipToday = bmsTipService.getRandomTip();
        return ApiResult.success(tipToday);
    }
}
