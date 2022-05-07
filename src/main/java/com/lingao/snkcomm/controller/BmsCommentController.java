package com.lingao.snkcomm.controller;

import com.lingao.snkcomm.common.api.ApiResult;
import com.lingao.snkcomm.model.dto.CommentDTO;
import com.lingao.snkcomm.model.entity.BmsComment;
import com.lingao.snkcomm.model.entity.UmsUser;
import com.lingao.snkcomm.model.vo.CommentVO;
import com.lingao.snkcomm.service.IBmsCommentService;
import com.lingao.snkcomm.service.IUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.lingao.snkcomm.jwt.JwtUtil.USER_NAME;
/**
 * @author lingao.
 * @description
 * @date 2022/5/7 - 16:39
 */
@RestController
@RequestMapping("/comment")
public class BmsCommentController extends BaseController {

    @Autowired
    private IBmsCommentService bmsCommentService;
    @Autowired
    private IUmsUserService umsUserService;

    @RequestMapping(value = "/getComments", method = RequestMethod.GET)
    public ApiResult<List<CommentVO>> getCommentsByTopicID(@RequestParam(value = "topicid") String topicid) {
        List<CommentVO> lstBmsComment = bmsCommentService.getCommentsByTopicID(topicid);
        return ApiResult.success(lstBmsComment);
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ApiResult<BmsComment> addComment(@RequestHeader(value = USER_NAME) String userName, @RequestBody CommentDTO dto) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        BmsComment comment = bmsCommentService.create(dto, user);
        return ApiResult.success(comment);
    }
}
