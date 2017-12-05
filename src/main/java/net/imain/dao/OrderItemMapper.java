package net.imain.dao;

import net.imain.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    /**
     * 根据用户的ID和订单号查询订单详情
     *
     * @param userId 用户ID
     * @param orderNo 订单号
     * @return 订单详情列表
     * TODO 我觉得不是一个List。。订单号是唯一的，不可能有一个列表啊
     */
    List<OrderItem> selectByUserIdAndOrderNo(@Param("userId") Integer userId,
                                             @Param("orderNo") Long orderNo);
}