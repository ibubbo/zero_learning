package net.imain.service.impl;

import net.imain.common.Const;
import net.imain.common.ResponseEnum;
import net.imain.common.ServerResponse;
import net.imain.common.TokenCache;
import net.imain.dao.UserMapper;
import net.imain.pojo.User;
import net.imain.service.IUserService;
import net.imain.util.MD5Util;
import net.imain.vo.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户模块服务层
 *
 * @author: uncle
 * @apdateTime: 2017-11-16 16:57
 */
@Service("iUserService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<UserInfoVo> login(String username, String password) {
        // 校验用户名
        int resultCount = userMapper.checkUserName(username);
        if (resultCount == 0) {
            return ServerResponse.error("用户名不存在");
        }
        String md5EncodeUtf8 = MD5Util.MD5EncodeUtf8(password);
        // 检查密码是否正确
        User resultUser = userMapper.selectLogin(username, md5EncodeUtf8);
        if (resultUser == null) {
            return ServerResponse.error("密码不正确");
        }
        // 密码置空
        resultUser.setPassword(StringUtils.EMPTY);
        resultUser.setPhone(null);
        // 数据准备
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(resultUser, vo);
        return ServerResponse.success("登录成功", vo);
    }

    @Override
    public ServerResponse<String> register(User user) {
        // 校验用户名
        ServerResponse serverResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        // 校验邮箱
        serverResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        // 补全用户信息
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        // 判断是否成功添加
        Integer resultSum = userMapper.insert(user);
        if (resultSum == 0) {
            return ServerResponse.error("注册失败");
        }
        return ServerResponse.success("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        // 数据判空
        if (!StringUtils.isNotBlank(type)) {
            return ServerResponse.error(ResponseEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 校验 type 是否合法
        if (!Const.USERNAME.equals(type) && !Const.EMAIL.equals(type)) {
            return ServerResponse.error(ResponseEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 如果是用户名
        if (Const.USERNAME.equals(type)) {
            Integer resultCount = userMapper.checkUserName(str);
            if (resultCount > 0) {
                return ServerResponse.error("用户名已存在");
            }
        }
        // 如果是邮箱
        if (Const.EMAIL.equals(type)) {
            Integer resultCount = userMapper.checkEmail(str);
            if (resultCount > 0) {
                return ServerResponse.error("邮箱已存在");
            }
        }
        return ServerResponse.success("校验成功");
    }

    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        // 1. 判断用户名是否存在
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.error("用户不存在");
        }
        // 2. 判断问题是否存在
        String getQuestion = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(getQuestion)) {
            return ServerResponse.error("该用户未设置找回密码问题");
        }
        return ServerResponse.success(getQuestion);
    }

    @Override
    public ServerResponse<String> forgetCheckAnswer(String username,
                                                    String question, String answer) {
        int selectAnswer = userMapper.selectAnswer(username, question, answer);
        if (selectAnswer == 0) {
            return ServerResponse.error("找回失败，请检查密保问题或密保答案是否正确");
        }
        String token = UUID.randomUUID().toString();
        TokenCache.setKey("token_" + username, token);
        return ServerResponse.success(token);
    }
}
