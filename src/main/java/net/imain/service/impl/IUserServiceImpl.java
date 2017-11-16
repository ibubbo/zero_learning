package net.imain.service.impl;

import net.imain.common.ServerResponse;
import net.imain.dao.UserMapper;
import net.imain.pojo.User;
import net.imain.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: uncle
 * @apdateTime: 2017-11-16 16:57
 */
@Service("iUserService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        // 校验用户名
        System.err.println(username + "........................");
        int resultCount = userMapper.checkUserName(username);
        System.err.println(resultCount + "--------------------");
        System.err.println(resultCount + " ****************** ");
        if (resultCount == 0) {
            return ServerResponse.error("用户名不存在");
        }
        // TODO 密码登录 MD5

        // 检查密码是否正确
        User resultUser = userMapper.selectLogin(username, password);
        if (resultUser == null) {
            return ServerResponse.error("密码不正确");
        }
        // 密码置空
        resultUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.success("登录成功", resultUser);
    }
}
