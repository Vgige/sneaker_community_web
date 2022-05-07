package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.common.exception.ApiException;
import com.lingao.snkcomm.model.dto.CreateTopicDTO;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.PostVO;
import com.lingao.snkcomm.service.IBmsPostService;
import com.lingao.snkcomm.service.IUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.lingao.snkcomm.jwt.JwtUtil.USER_NAME;
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
    @Autowired
    private IUmsUserService umsUserService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = bmsPostService.getList(new Page<>(pageNo, pageSize), tab);
        return ApiResult.success(list);
    }
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<BmsPost> create(@RequestHeader(value = USER_NAME) String userName, @RequestBody CreateTopicDTO dto) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        BmsPost topic = null;
        try{
            topic = bmsPostService.create(dto, user);
        }catch (ApiException e){
            return ApiResult.failed(e.getMessage());
        }
        return ApiResult.success(topic);
    }
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ApiResult<Map<String, Object>> view(@RequestParam("id") String id) {
        Map<String, Object> map = bmsPostService.viewTopic(id);
        return ApiResult.success(map);
    }
    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    public ApiResult<List<BmsPost>> getRecommend(@RequestParam("topicId") String id) {
        List<BmsPost> topics = bmsPostService.getRecommend(id);
        return ApiResult.success(topics);
    }
}
