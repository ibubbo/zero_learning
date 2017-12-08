package net.imain.service;

import com.github.pagehelper.PageInfo;
import net.imain.common.HandlerResult;
import net.imain.vo.OrderVo;

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

    /**
     * 创建订单
     *
     * @param userId
     * @param shippingId
     * @return
     */
    HandlerResult createOrder(Integer userId, Integer shippingId);

    /**
     * 订单取消
     *
     * @param userId
     * @param orderNo
     * @return
     */
    HandlerResult<String> cancel(Integer userId, Long orderNo);

    /**
     * 查询订单详细信息
     *
     * @param userId
     * @return
     */
    HandlerResult getOrderCartProduct(Integer userId);

    /**
     * 获取订单详情
     *
     * @param userId
     * @param orderNo
     * @return
     */
    HandlerResult detail(Integer userId, Long orderNo);

    /**
     * 获取用户订单详情(分页)
     *
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return
     */
    HandlerResult<PageInfo> list(Integer userId, Integer pageSize, Integer pageNum);

    /**
     * 后台订单详情分页（管理）
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    HandlerResult<PageInfo> manageList(Integer pageSize, Integer pageNum);

    /**
     * 根据订单号获取订单详情
     *
     * @param orderNo 订单号
     * @return
     */
    HandlerResult<OrderVo> managerDetail(Long orderNo);

    /**
     * 根据订单号搜索订单详情
     *
     * @param orderNo 订单号
     * @return
     */
    HandlerResult<PageInfo> managerSearch(Long orderNo, Integer pageSize, Integer pageNum);

    /**
     * 订单发货
     *
     * @param orderNo
     * @return
     */
    HandlerResult<String> sendGoods(Long orderNo);
}
