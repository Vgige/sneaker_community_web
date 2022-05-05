package com.lingao.snkcomm.controller;

import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.dto.LoginDTO;
import com.lingao.snkcomm.model.dto.RegisterDTO;
import com.lingao.snkcomm.model.entity.UmsUser;
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
    IUmsUserService umsUserService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        UmsUser user = umsUserService.executeRegister(dto);
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("账号注册失败");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        String token = umsUserService.executeLogin(dto);
        if (ObjectUtils.isEmpty(token)) {
            return ApiResult.failed("登录异常");
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
}
