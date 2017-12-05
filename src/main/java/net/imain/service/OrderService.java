package net.imain.service;

import net.imain.common.HandlerResult;

import java.util.Map;

/**
 * @author: uncle
 * @apdateTime: 2017-12-04 17:08
 */
public interface OrderService {

    /**
     * 支付
     *
     * @param userId 用户ID
     * @param orderNo 订单号
     * @param path 二维码工程路径
     * @param imgPath 二维码服务器路径
     * @return 成功或失败
     */
    HandlerResult pay(Integer userId, Long orderNo, String path, String imgPath);

    /**
     * 支付宝回调接口
     *
     * @param params 回调参数
     * @return 成功或失败
     */
    HandlerResult aliCallback(Map<String, String> params);

    /**
     * 查询订单状态
     *
     * @param userId 用户ID
     * @param orderNo 订单号
     * @return 成功或失败
     */
    HandlerResult queryOrderPayStatus(Integer userId, Long orderNo);
}
