package com.lingao.snkcomm.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingao.snkcomm.mapper.BmsBillboardMapper;
import com.lingao.snkcomm.mapper.BmsTipMapper;
import com.lingao.snkcomm.model.entity.BmsBillboard;
import com.lingao.snkcomm.model.entity.BmsTip;
import com.lingao.snkcomm.service.IBmsBillboardService;
import com.lingao.snkcomm.service.IBmsTipService;
import org.springframework.stereotype.Service;

/**
 * @author lingao.
 * @description
 * @date 2022/5/2 - 16:37
 */
@Service
public class BmsTipServiceImpl extends ServiceImpl<BmsTipMapper, BmsTip> implements IBmsTipService {

    @Override
    public BmsTip getRandomTip() {
        return baseMapper.getRandomTip();
    }
}
