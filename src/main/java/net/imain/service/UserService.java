package net.imain.service;

import net.imain.common.HandlerResult;
import net.imain.pojo.User;
import net.imain.vo.UserInfoVo;

/**
 * @author: uncle
 * @apdateTime: 2017-11-16 16:55
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    HandlerResult<UserInfoVo> login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户详细信息
     * @return
     */
    HandlerResult<String> register(User user);

    /**
     * 校验用户名和邮箱
     *
     * @param str 真实的值
     * @param type 校验类型：username/email
     * @return 失败或成功
     */
    HandlerResult<String> checkValid(String str, String type);

    /**
     * 根据用户名得到用户密保问题
     *
     * @param username
     * @return
     */
    HandlerResult<String> forgetGetQuestion(String username);

    /**
     * 找回密码
     *
     * @param username 用户名
     * @param question 密保问题
     * @param answer 密保答案
     * @return
     */
    HandlerResult<String> forgetCheckAnswer(String username,
                                            String question, String answer);

    /**
     * 忘记密码的重设密码
     *
     * @param username 用户名
     * @param passwordNew 新密码
     * @param forgetToken 用户的token
     * @return
     */
    HandlerResult<String> forgetResetPassword(String username,
                                              String passwordNew, String forgetToken);

    /**
     * 登录状态的重设密码
     *
     * @param user 用户信息
     * @param passwordOld 用户旧密码
     * @param passwordNew 用户新密码
     * @return
     */
    HandlerResult<String> resetPassword(UserInfoVo user,
                                               String passwordOld, String passwordNew);

    /**
     * 登录状态下更新个人信息
     *
     * @param user 用户信息
     * @return
     */
    HandlerResult<User> updateInformation(User user);

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return
     */
    HandlerResult<User> getInformation(Integer userId);

    /**
     * 校验用户权限
     *
     * @param user 需要校验的用户
     * @return
     */
    HandlerResult checkAdminRole(UserInfoVo user);
}
