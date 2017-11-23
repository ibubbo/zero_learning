package net.imain.service;

import net.imain.common.HandlerResult;
import net.imain.vo.CartResultVo;

/**
 * @author: uncle
 * @apdateTime: 2017-11-23 10:23
 */
public interface CartService {

    /**
     * 添加购物车
     *
     * @param productId
     * @param count
     * @param userId
     * @return
     */
    HandlerResult<CartResultVo> add(Integer productId, Integer count, Integer userId);
}
