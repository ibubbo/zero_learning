package net.imain.dao;

import net.imain.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    /**
     * 根据用户ID和地址ID删除地址信息
     *
     * @param shippingId 地址ID
     * @param userId 用户ID
     * @return 成功删除的条数
     */
    int deleteByShippingIdAndUserId(@Param("userId") Integer userId,
                                    @Param("shippingId") Integer shippingId);

    /**
     * 根据地址信息修改地址
     *
     * @param record
     * @return
     */
    int updateByShipping(Shipping record);

    /**
     * 根据地址ID和用户ID查询地址信息
     *
     * @param shippingId 地址ID
     * @param userId 用户ID
     * @return 地址详情
     */
    Shipping selectByShippingIdAndUserId(@Param("userId") Integer userId,
                                         @Param("shippingId") Integer shippingId);

    /**
     * 根据用户ID查询用户的地址信息
     *
     * @param userId
     * @return
     */
    List<Shipping> selectByUserId(Integer userId);
}