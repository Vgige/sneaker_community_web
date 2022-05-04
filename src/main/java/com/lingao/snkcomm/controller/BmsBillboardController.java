package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.entity.BmsBillboard;
import com.lingao.snkcomm.service.IBmsBillboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/2 - 17:31
 */
@RestController
@RequestMapping("/billboard")
public class BmsBillboardController extends BaseController{
    @Autowired
    private IBmsBillboardService bmsBillboardService;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ApiResult<BmsBillboard> getNotices(){
        List<BmsBillboard> list = bmsBillboardService.list(new LambdaQueryWrapper<BmsBillboard>().eq(BmsBillboard::isShow,true));
        return ApiResult.success(list.get(list.size()- 1));
    }
}
