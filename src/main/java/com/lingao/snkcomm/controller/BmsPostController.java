package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.vo.PostVO;
import com.lingao.snkcomm.service.IBmsPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 16:38
 */
@RestController
@RequestMapping("/post")
public class BmsPostController extends BaseController{
    @Autowired
    private IBmsPostService bmsPostService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = bmsPostService.getList(new Page<>(pageNo, pageSize), tab);
        return ApiResult.success(list);
    }
}
