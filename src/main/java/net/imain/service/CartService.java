package net.imain.service;

import net.imain.common.HandlerResult;
import net.imain.vo.CartResultVo;

/**
 * @author: uncle
 * @apdateTime: 2017-11-23 10:23
 */
public interface CartService {

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

    /**
     * 报错或修改商品
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    HandlerResult saveOrUpdate(Integer userId, Integer productId, Integer count);

    /**
     * 全选或全不选
     *
     * @param userId 用户ID
     * @param isSelectAll true 全选，false 全不选
     * @return
     */
    HandlerResult<CartResultVo> selectAllOrUnSelectAll(Integer userId, boolean isSelectAll);

    /**
     * 选或不选
     *
     * @param userId
     * @param productId
     * @param isSelect
     * @return
     */
    HandlerResult<CartResultVo> selectOrUnSelect(Integer userId, Integer productId, boolean isSelect);

    /**
     * 得到用户商品总数
     *
     * @param userId
     * @return
     */
    HandlerResult<Integer> getCartProductCount(Integer userId);

}
