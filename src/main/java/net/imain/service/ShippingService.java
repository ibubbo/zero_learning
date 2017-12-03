package net.imain.service;

import com.github.pagehelper.PageInfo;
import net.imain.common.HandlerResult;
import net.imain.pojo.Shipping;

/**
 * @author: uncle
 * @apdateTime: 2017-12-03 10:22
 */
public interface ShippingService {
    /**
     * 更新地址
     *
     * @param userId 用户ID
     * @param shipping 地址对象
     * @return 成功或失败
     */
    HandlerResult saveOrUpdate(Integer userId, Shipping shipping);

    /**
     * 删除地址
     *
     * @param shippingId 地址ID
     * @param userId 用户ID
     * @return 成功或失败
     */
    HandlerResult del(Integer userId, Integer shippingId);

    /**
     * 根据用户ID和地址ID查询地址信息
     *
     * @param userId 用户ID
     * @param shippingId 地址ID
     * @return 地址信息
     */
    HandlerResult<Shipping> select(Integer userId, Integer shippingId);

    /**
     * 获取所有地址信息并分页显示
     *
     * @param pageNum 开始页，默认 1
     * @param pageSize 每页条数，默认 10
     * @return 地址列表
     */
    HandlerResult<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
