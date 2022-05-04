package com.lingao.snkcomm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingao.snkcomm.model.entity.BmsBillboard;
import com.lingao.snkcomm.model.entity.BmsTip;

/**
 * @author lingao.
 * @description
 * @date 2022/5/2 - 16:35
 */

public interface IBmsTipService extends IService<BmsTip> {
    BmsTip getRandomTip();
}
