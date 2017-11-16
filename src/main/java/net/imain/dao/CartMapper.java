package net.imain.dao;

import net.imain.pojo.Cart;

/**
 * 购物车接口层
 *
 * @author uncle
 */
public interface CartMapper {
    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return 删除成功的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加购物车
     *
     * @param record 购物车对象
     * @return 添加成功的数量
     */
    int insert(Cart record);

    /**
     * 根据购物车中的信息添加购物车（添加不为空的字段）
     *
     * @param record 购物车
     * @return 添加成功的数量
     */
    int insertSelective(Cart record);

    /**
     * 根据主键查询购物车信息
     *
     * @param id 主键
     * @return 购物车信息
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * 根据传入的字段来更新购物车（修改不为空的字段信息）
     *
     * @param record 购物车对象
     * @return 修改成功的数量
     */
    int updateByPrimaryKeySelective(Cart record);

    /**
     * 更新购物车信息
     *
     * @param record 购物车
     * @return 修改成功的数量
     */
    int updateByPrimaryKey(Cart record);
}