package net.imain.dao;

import net.imain.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 根据用户ID查询订单号
     *
     * @param userId 用户ID
     * @param orderNo 订单号
     * @return 订单对象Order
     */
    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId,
                                   @Param("orderNo") long orderNo);

    /**
     * 根据订单号查询订单
     *
     * @param orderNo
     * @return
     */
    Order selectByOrderNo(Long orderNo);
}