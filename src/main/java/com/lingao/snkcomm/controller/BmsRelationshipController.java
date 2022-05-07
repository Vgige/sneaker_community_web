package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.common.exception.ApiAsserts;
import com.lingao.snkcomm.model.entity.BmsFollow;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.service.IBmsFollowService;
import com.lingao.snkcomm.service.IUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.lingao.snkcomm.jwt.JwtUtil.USER_NAME;
/**
 * @author lingao.
 * @description
 * @date 2022/5/7 - 13:18
 */


@RestController
@RequestMapping("/relationship")
public class BmsRelationshipController extends BaseController{
    @Autowired
    private IUmsUserService umsUserService;
    @Autowired
    private IBmsFollowService bmsFollowService;

    @RequestMapping(value = "/subscribe/{userId}", method = RequestMethod.GET)
    public ApiResult<Object> handleFollow(@RequestHeader(value = USER_NAME) String userName, @PathVariable("userId") String parentId) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        if (parentId.equals(umsUser.getId())) {
            return ApiResult.failed("不能关注自己哦！");
        }
        BmsFollow one = bmsFollowService.getOne(
                new LambdaQueryWrapper<BmsFollow>()
                        .eq(BmsFollow::getParentId, parentId)
                        .eq(BmsFollow::getFollowerId, umsUser.getId()));
        if (!ObjectUtils.isEmpty(one)) {
            return ApiResult.failed("已关注");
        }

        BmsFollow follow = new BmsFollow();
        follow.setParentId(parentId);
        follow.setFollowerId(umsUser.getId());
        bmsFollowService.save(follow);
        return ApiResult.success(null, "关注成功!");
    }

    @RequestMapping(value = "/unsubscribe/{userId}", method = RequestMethod.GET)
    public ApiResult<Object> handleUnFollow(@RequestHeader(value = USER_NAME) String userName, @PathVariable("userId") String parentId) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        BmsFollow one = bmsFollowService.getOne(
                new LambdaQueryWrapper<BmsFollow>()
                        .eq(BmsFollow::getParentId, parentId)
                        .eq(BmsFollow::getFollowerId, umsUser.getId()));
        if (ObjectUtils.isEmpty(one)) {
            return ApiResult.failed("未关注该用户！");
        }
        bmsFollowService.remove(new LambdaQueryWrapper<BmsFollow>().eq(BmsFollow::getParentId, parentId)
                .eq(BmsFollow::getFollowerId, umsUser.getId()));
        return ApiResult.success(null, "取关成功！");
    }

    @RequestMapping(value = "/validate/{topicUserId}", method = RequestMethod.GET)
    public ApiResult<Map<String, Object>> isFollow(@RequestHeader(value = USER_NAME) String userName, @PathVariable("topicUserId") String topicUserId) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        Map<String, Object> map = new HashMap<>(16);
        map.put("hasFollow", false);
        if (!ObjectUtils.isEmpty(umsUser)) {
            BmsFollow one = bmsFollowService.getOne(new LambdaQueryWrapper<BmsFollow>()
                    .eq(BmsFollow::getParentId, topicUserId)
                    .eq(BmsFollow::getFollowerId, umsUser.getId()));
            if (!ObjectUtils.isEmpty(one)) {
                map.put("hasFollow", true);
            }
        }
        return ApiResult.success(map);
    }
}
