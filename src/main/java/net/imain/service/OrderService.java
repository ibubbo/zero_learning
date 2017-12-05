package net.imain.service;

import net.imain.common.HandlerResult;

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
     * @return
     */
    HandlerResult pay(Integer userId, Long orderNo, String path, String imgPath);
}
