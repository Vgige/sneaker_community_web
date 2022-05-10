package com.lingao.snkcomm.controller;

import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.service.IUmsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.lingao.snkcomm.jwt.JwtUtil.USER_NAME;

/**
 * @author lingao.
 * @description 文件上传
 * @date 2022/5/9 - 16:54
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileUploadController extends BaseController{
    @Autowired
    private IUmsUserService umsUserService;
    @Value("${file.uploadurl}")
    private String uploadPath;

    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public ApiResult<Object> importData(@RequestHeader(value = USER_NAME) String userName, MultipartFile file, HttpServletRequest req) throws IOException {
        UmsUser user = umsUserService.getUserByUsername(userName);

        String url = null;

        File folder = new File(uploadPath);
        if (!folder.isDirectory()) {
            //递归生成文件夹
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        file.transferTo(new File(folder,newName));
        url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload/" + newName;
        System.out.println(url);


        if(url != null){
            user.setAvatar(url);
            umsUserService.updateById(user);
        }
        return ApiResult.success("上传成功");
    }
}
