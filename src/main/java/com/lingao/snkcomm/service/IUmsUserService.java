package com.lingao.snkcomm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingao.snkcomm.model.dto.LoginDTO;
import com.lingao.snkcomm.model.dto.RegisterDTO;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.ProfileVO;

/**
 * @author lingao.
 * @description
 * @date 2022/5/2 - 16:35
 */

public interface IUmsUserService extends IService<UmsUser> {
    /**
     * 注册功能
     * @param dto
     * @return
     */
    UmsUser executeRegister(RegisterDTO dto);
    /**
     * 获取用户信息
     *
     * @param username
     * @return dbUser
     */
    UmsUser getUserByUsername(String username);
    /**
     * 用户登录
     *
     * @param dto
     * @return 生成的JWT的token
     */
    String executeLogin(LoginDTO dto);
    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return
     */
    ProfileVO getUserProfile(String id);
}
