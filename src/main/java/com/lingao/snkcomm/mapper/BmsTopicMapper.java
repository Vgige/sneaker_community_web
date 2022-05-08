package com.lingao.snkcomm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingao.snkcomm.model.entity.BmsPost;
import com.lingao.snkcomm.model.vo.PostVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lingao.
 * @description
 * @date 2022/5/5 - 15:47
 */
@Repository
public interface BmsTopicMapper extends BaseMapper<BmsPost> {
    /**
     * 分页查询首页话题列表
     * <p>
     *
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> selectListAndPage(@Param("page") Page<PostVO> page, @Param("tab") String tab);

    /**
     * 获取详情页推荐
     *
     * @param id
     * @return
     */
    List<BmsPost> selectRecommend(@Param("id") String id);
    /**
     * 全文检索
     *
     * @param page
     * @param keyword
     * @return
     */
    Page<PostVO> searchByKey(@Param("page") Page<PostVO> page, @Param("keyword") String keyword);

    Page<PostVO> selectListByTagName(@Param("page") Page<PostVO> page, @Param("id") String id);
}
