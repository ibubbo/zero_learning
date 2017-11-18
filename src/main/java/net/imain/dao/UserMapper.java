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
     * 校验用户名
     *
     * @param username
     * @return 存在返 1, 不存在返 0
     */
    Integer checkUserName(@Param("username") String username);

    /**
     * 校验邮箱
     *
     * @param email
     * @return
     */
    Integer checkEmail(@Param("email") String email);

    /**
     * 根据用户ID校验用户邮箱
     *
     * @param email
     * @param userId
     * @return
     */
    Integer checkEmailByUserId(@Param("email") String email,
                               @Param("userId") Integer userId);

    /**
     * 根据用户id校验用户密码
     *
     * @param password
     * @param userId
     * @return
     */
    Integer checkPassword(@Param("password") String password,
                          @Param("userId") Integer userId);

    /**
     * 查询用户信息
     *
     * @param username
     * @param password
     * @return 用户详细信息
     */
    User selectLogin(@Param("username") String username,
                     @Param("password") String password);

    /**
     * 根据用户名得到密保问题
     *
     * @param username
     * @return
     */
    String selectQuestionByUsername(String username);

    /**
     * 找回密码
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    Integer selectAnswer(@Param("username") String username,
                     @Param("question") String question,
                     @Param("answer") String answer);

    /**
     * 根据用户名修改用户密码
     *
     * @param username 被修改的用户名
     * @param password 新密码
     * @return 修改成功的条数
     */
    Integer updatePasswordByUsername(@Param("username") String username,
                                     @Param("password") String password);

}