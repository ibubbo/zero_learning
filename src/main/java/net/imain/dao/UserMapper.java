package net.imain.dao;

import net.imain.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 校验用户名是否存在
     *
     * @param username
     * @return 存在返 1, 不存在返 0
     */
    Integer checkUserName(@Param("username") String username);

    /**
     * 查询用户信息
     *
     * @param username
     * @param password
     * @return 用户详细信息
     */
    User selectLogin(@Param("username") String username,
                     @Param("password") String password);
}