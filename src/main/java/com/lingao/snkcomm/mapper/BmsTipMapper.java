package com.lingao.snkcomm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingao.snkcomm.model.entity.BmsTip;
import org.springframework.stereotype.Repository;

/**
 * @author lingao.
 * @description
 * @date 2022/5/3 - 12:52
 */
@Repository
public interface BmsTipMapper extends BaseMapper<BmsTip> {
    BmsTip getRandomTip();
}
