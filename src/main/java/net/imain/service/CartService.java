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
//    HandlerResult<CartResultVo> add(Integer productId, Integer count, Integer userId);

    /**
     * 获取用户购物车信息
     *
     * @param userId
     * @return
     */
    HandlerResult list(Integer userId);

    /**
     * 更新购物车
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    HandlerResult update(Integer userId, Integer productId, Integer count);

    /**
     * 根据用户ID和商品ID删除购物车
     *
     * @param userId
     * @param productIds
     * @return
     */
    HandlerResult delete(Integer userId, String productIds);

    HandlerResult saveOrUpdate(Integer userId, Integer productId, Integer count);
}
