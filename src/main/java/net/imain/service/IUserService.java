package net.imain.service;

import net.imain.common.ServerResponse;
import net.imain.pojo.User;
import net.imain.vo.UserInfoVo;

/**
 * @author: uncle
 * @apdateTime: 2017-11-16 16:55
 */
public interface IUserService {

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    ServerResponse<UserInfoVo> login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户详细信息
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 校验用户名和邮箱
     *
     * @param str 真实的值
     * @param type 校验类型：username/email
     * @return 失败或成功
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 根据用户名得到用户密保问题
     *
     * @param username
     * @return
     */
    ServerResponse<String> forgetGetQuestion(String username);

    /**
     * 找回密码
     *
     * @param username 用户名
     * @param question 密保问题
     * @param answer 密保答案
     * @return
     */
    ServerResponse<String> forgetCheckAnswer(String username,
                                                    String question, String answer);
}
