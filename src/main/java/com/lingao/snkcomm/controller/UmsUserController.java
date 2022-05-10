package com.lingao.snkcomm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.common.exception.ApiException;
import com.lingao.snkcomm.model.dto.LoginDTO;
import com.lingao.snkcomm.model.dto.RegisterDTO;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.service.IBmsPostService;
import com.lingao.snkcomm.service.IUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
//import static: 导入类下的静态方法或者静态属性且访问控制符不能为private
import static com.lingao.snkcomm.jwt.JwtUtil.USER_NAME;
/**
 * @author lingao.
 * @description
 * @date 2022/5/4 - 11:47
 */
@RestController
@RequestMapping("/ums/user")
public class UmsUserController extends BaseController{
    @Autowired
    private IUmsUserService umsUserService;
    @Autowired
    private IBmsPostService bmsPostService;
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        UmsUser user = null;
        try{
            user = umsUserService.executeRegister(dto);
        }catch (ApiException e){
            return ApiResult.failed(e.getMessage());
        }
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("账号注册失败");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        String token = null;
        try{
            token = umsUserService.executeLogin(dto);
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("token", token);
        return ApiResult.success(map, "登录成功");
    }
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<UmsUser> getUser(@RequestHeader(value = USER_NAME) String userName) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        return ApiResult.success(user);
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResult<Object> logOut() {
        return ApiResult.success(null, "注销成功");
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ApiResult<Map<String, Object>> getUserByName(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> map = new HashMap<>(16);
        UmsUser user = umsUserService.getUserByUsername(username);
        if(ObjectUtils.isEmpty(user)){
            return ApiResult.failed("用户不存在");
        }
        Page<BmsPost> page = bmsPostService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getUserId, user.getId()));
        map.put("user", user);
        map.put("topics", page);
        return ApiResult.success(map);
    }
    @RequestMapping(value = "/{update}", method = RequestMethod.POST)
    public ApiResult<UmsUser> updateUser(@RequestBody UmsUser umsUser) {
        umsUserService.updateById(umsUser);
        return ApiResult.success(umsUser);
    }
}
