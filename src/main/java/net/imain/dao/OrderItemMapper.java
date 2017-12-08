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
     */
    List<OrderItem> selectByUserIdAndOrderNo(@Param("userId") Integer userId,
                                             @Param("orderNo") Long orderNo);


    List<OrderItem> selectByOrderNo(@Param("orderNo") Long orderNo);

    /**
     * 批量插入订单详情
     *
     * @param orderItemList
     */
    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

    /**
     * 根据用户名查询订单详情
     *
     * @param userId
     * @return
     */
    List<OrderItem> selectByUserId(@Param("userId") Integer userId);
}