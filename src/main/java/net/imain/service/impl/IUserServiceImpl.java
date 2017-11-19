package net.imain.service.impl;

import net.imain.common.Const;
import net.imain.common.HandlerConverter;
import net.imain.enums.HandlerEnum;
import net.imain.enums.UserEnum;
import net.imain.common.HandlerResult;
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
    public HandlerResult<UserInfoVo> login(String username, String password) {
        // 校验用户名
        int resultCount = userMapper.checkUserName(username);
        if (resultCount == 0) {
            return HandlerResult.error(UserEnum.USERNAME_NOT_EXIST.getMessage());
        }
        String md5EncodeUtf8 = MD5Util.MD5EncodeUtf8(password);
        // 检查密码是否正确
        User resultUser = userMapper.selectLogin(username, md5EncodeUtf8);
        if (resultUser == null) {
            return HandlerResult.error(UserEnum.PASSWORD_ERROR.getMessage());
        }
        // 密码置空
        resultUser.setPassword(StringUtils.EMPTY);
        resultUser.setPhone(null);
        // 数据准备
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(resultUser, vo);
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage(), vo);
    }

    @Override
    public HandlerResult<String> register(User user) {
        // 校验用户名
        HandlerResult serverResponse = this.checkValid(user.getUsername(), Const.USERNAME);
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
            return HandlerResult.error(UserEnum.REGISTER_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<String> checkValid(String str, String type) {
        // 数据判空
        if (StringUtils.isBlank(type)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 校验 type 是否合法
        if (!Const.USERNAME.equals(type) && !Const.EMAIL.equals(type)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 如果是用户名
        if (Const.USERNAME.equals(type)) {
            Integer resultCount = userMapper.checkUserName(str);
            if (resultCount > 0) {
                return HandlerResult.error(UserEnum.USERNAME_EXIST.getMessage());
            }
        }
        // 如果是邮箱
        if (Const.EMAIL.equals(type)) {
            Integer resultCount = userMapper.checkEmail(str);
            if (resultCount > 0) {
                return HandlerResult.error(UserEnum.EMAIL_EXIST.getMessage());
            }
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<String> forgetGetQuestion(String username) {
        // 1. 判断用户名是否存在
        HandlerResult<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return HandlerResult.error(UserEnum.USERNAME_NOT_EXIST.getMessage());
        }
        // 2. 判断问题是否存在
        String getQuestion = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(getQuestion)) {
            return HandlerResult.error(UserEnum.QUESTION_IS_NULL.getMessage());
        }
        return HandlerResult.success(getQuestion);
    }

    @Override
    public HandlerResult<String> forgetCheckAnswer(String username,
                                                   String question, String answer) {
        // 校验用户名
        HandlerResult<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return HandlerResult.error(UserEnum.USERNAME_NOT_EXIST.getMessage());
        }
        // 校验密保问题和答案
        int selectAnswer = userMapper.selectAnswer(username, question, answer);
        if (selectAnswer == 0) {
            return HandlerResult.error(UserEnum.GET_BACK_PASSWORD_ERROR.getMessage());
        }
        // 使用UUID生成Token并且加入本地缓存
        String token = UUID.randomUUID().toString();
        TokenCache.setKey(Const.TOKEN_PREFIX + username, token);
        return HandlerResult.success(token);
    }

    @Override
    public HandlerResult<String> forgetResetPassword(String username,
                                                     String passwordNew, String forgetToken) {
        // 校验token
        if (StringUtils.isBlank(forgetToken)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 校验用户名
        HandlerResult<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return HandlerResult.error(UserEnum.USERNAME_NOT_EXIST.getMessage());
        }
        String tokenUser = TokenCache.getKey(Const.TOKEN_PREFIX + username);
        // 校验token
        if (StringUtils.isBlank(tokenUser)) {
            return HandlerResult.error(UserEnum.TOKEN_INEFFECTIVENESS.getMessage());
        }
        if (!StringUtils.equals(forgetToken, tokenUser)) {
            return HandlerResult.error(UserEnum.TOKEN_INEFFECTIVENESS.getMessage());
        }
        // 重设密码
        String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
        Integer resultSum = userMapper.updatePasswordByUsername(username, md5Password);
        if (resultSum == 0) {
            return HandlerResult.error(UserEnum.UPDATE_PASSWORD_ERROR.getMessage());
        }
        return HandlerResult.error(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<String> resetPassword(UserInfoVo userInfoVo,
                                               String passwordOld, String passwordNew) {
        // UserInfoVo -> User
        User user = HandlerConverter.userInfoToUser(userInfoVo);
        // 防止横向越权，要校验一下用户的旧密码，密码一定要记得是MD5加密后的
        Integer resultSum = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultSum == 0) {
            return HandlerResult.error(UserEnum.OLD_PASSWORD_ERROR.getMessage());
        }
        // 设置新密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateSum = userMapper.updateByPrimaryKeySelective(user);
        if (updateSum == 0) {
            return HandlerResult.success(UserEnum.UPDATE_PASSWORD_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<User> updateInformation(User user) {
        // 校验邮箱，要更改的邮箱不能是其他用户ID已经拥有的
        Integer resultSum = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultSum > 0) {
            return HandlerResult.error(UserEnum.EMAIL_EXIST.getMessage());
        }
        // 修改（用户名不能被修改）
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        Integer updateUserSum = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateUserSum == 0) {
            return HandlerResult.error(UserEnum.UPDATE_USERINFO_ERROR.getMessage());
        }

        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage(), updateUser);
    }

    @Override
    public HandlerResult<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return HandlerResult.error(UserEnum.NOT_FIND_USERINFO.getMessage());
        }
        user.setPassword(StringUtils.EMPTY);
        return HandlerResult.success(user);
    }

    @Override
    public HandlerResult checkAdminRole(UserInfoVo user) {
        if (!(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN)) {
            return HandlerResult.error();
        }
        return HandlerResult.success();
    }
}
