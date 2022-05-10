package com.lingao.snkcomm.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.common.exception.ApiAsserts;
import com.lingao.snkcomm.jwt.JwtUtil;
import com.lingao.snkcomm.mapper.BmsBillboardMapper;
import com.lingao.snkcomm.mapper.BmsFollowMapper;
import com.lingao.snkcomm.mapper.BmsTopicMapper;
import com.lingao.snkcomm.mapper.UmsUserMapper;
import com.lingao.snkcomm.model.dto.EmailDTO;
import com.lingao.snkcomm.model.dto.LoginDTO;
import com.lingao.snkcomm.model.dto.RegisterDTO;
import com.lingao.snkcomm.model.entity.BmsBillboard;
import com.lingao.snkcomm.model.entity.BmsFollow;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.ProfileVO;
import com.lingao.snkcomm.service.*;
import com.lingao.snkcomm.utils.MD5Utils;
import com.lingao.snkcomm.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;

/**
 * @author lingao.
 * @description
 * @date 2022/5/2 - 16:37
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UmsUserServiceImpl extends ServiceImpl<UmsUserMapper, UmsUser> implements IUmsUserService {

    @Value("${code.expiration}")
    private Long expiration;

    @Autowired
    private BmsTopicMapper bmsTopicMapper;
    @Autowired
    private BmsFollowMapper bmsFollowMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IEmailService emailService;

    @Override
    public UmsUser executeRegister(RegisterDTO dto) {
        // 通过email获取redis中的code
        Object value = redisUtils.get(dto.getEmail());
        if (value == null || !value.toString().equals(dto.getCode())) {
            ApiAsserts.fail("验证码错误或已过期！");
        } else {
            redisUtils.del(dto.getEmail());
        }
        //查询是否有相同用户名的用户
        LambdaQueryWrapper<UmsUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUser::getUsername, dto.getName());
        UmsUser umsUser = baseMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(umsUser)) {
            ApiAsserts.fail("账号已存在！");
        }
        UmsUser addUser = UmsUser.builder()
                .username(dto.getName())
                .alias(dto.getName())
                .password(MD5Utils.getPwd(dto.getPass()))
                .email(dto.getEmail())
                .createTime(new Date())
                .status(true)
                .build();
        baseMapper.insert(addUser);

        return addUser;
    }
    @Override
    public UmsUser getUserByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<UmsUser>().eq(UmsUser::getUsername, username));
    }
    @Override
    public String executeLogin(LoginDTO dto) {
        String token = null;
        try {
            UmsUser user = this.getUserByUsername(dto.getUsername());
            if(user == null){
                throw new Exception("用户不存在");
            }
            String encodePwd = MD5Utils.getPwd(dto.getPassword());
            if(!encodePwd.equals(user.getPassword()))
            {
                throw new Exception("密码错误");
            }
            token = JwtUtil.generateToken(String.valueOf(user.getUsername()));
        } catch (Exception e) {
            log.warn("用户不存在or密码验证失败=======>{}", dto.getUsername());
        }
        return token;
    }

    @Override
    public ProfileVO getUserProfile(String id) {
        ProfileVO profile = new ProfileVO();
        UmsUser user = baseMapper.selectById(id);
        BeanUtils.copyProperties(user, profile);
        //用户文章数
        int count = bmsTopicMapper.selectCount(new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getUserId, id));
        profile.setTopicCount(count);

        //粉丝数
        int followers = bmsFollowMapper.selectCount((new LambdaQueryWrapper<BmsFollow>().eq(BmsFollow::getParentId, id)));
        profile.setFollowerCount(followers);

        return profile;
    }

    @Override
    public boolean registerEmailExist(String email) {
        QueryWrapper<UmsUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsUser::getEmail, email);
        return baseMapper.selectOne(queryWrapper) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMailCode(String email) {
        if(this.registerEmailExist(email)){
            ApiAsserts.fail("注册邮箱已存在");
        }
        // 获取发送邮箱验证码的HTML模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("mailtemplate.ftl");

        // 从redis缓存中尝试获取验证码
        Object code = redisUtils.get(email);
        if (code != null) {
            redisUtils.del(email);
        }
        // 如果在缓存中未获取到验证码，则产生6位随机数，放入缓存中
        code = RandomUtil.randomNumbers(6);
        if (!redisUtils.set(email, code, expiration)) {
            ApiAsserts.fail("后台缓存服务异常");
        }
        // 发送验证码
        emailService.send(new EmailDTO(Collections.singletonList(email),
                "『一个社区』注册验证码", template.render(Dict.create().set("code", code))));
    }
}
