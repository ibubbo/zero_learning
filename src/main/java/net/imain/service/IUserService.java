package net.imain.service;

import net.imain.common.ServerResponse;
import net.imain.pojo.User;

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
    ServerResponse<User> login(String username, String password);
}
