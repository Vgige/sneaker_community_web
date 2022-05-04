package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.entity.BmsBillboard;
import com.lingao.snkcomm.model.entity.BmsPromotion;
import com.lingao.snkcomm.service.IBmsBillboardService;
import com.lingao.snkcomm.service.IBmsPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/2 - 17:31
 */
@RestController
@RequestMapping("/promotion")
public class BmsPromotionController extends BaseController{
    @Autowired
    private IBmsPromotionService bmsPromotionService;

    @GetMapping("/all")
    public ApiResult<List<BmsPromotion>> getList(){
        List<BmsPromotion> list = bmsPromotionService.list();
        return ApiResult.success(list);
    }
}
