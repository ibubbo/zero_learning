package net.imain.controller.portal;

import net.imain.common.Const;
import net.imain.common.HandlerCheck;
import net.imain.enums.HandlerEnum;
import net.imain.common.HandlerResult;
import net.imain.pojo.User;
import net.imain.service.UserService;
import net.imain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: uncle
 * @apdateTime: 2017-11-16 16:43
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService iUserService;

    /**
     * 用户登录
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public HandlerResult<UserInfoVo> login(String username,
                                           String password, HttpSession session) {
        HandlerResult<UserInfoVo> user = iUserService.login(username, password);
        if (user.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, user.getData());
        }
        return user;
    }

    /**
     * 用户登出
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> logout(HttpSession session) {
        try {
            session.removeAttribute(Const.CURRENT_USER);
        } catch (Exception e) {
            return HandlerResult.error(HandlerEnum.SERVER_EXCEPTION.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "register.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 校验用户名和邮箱
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户的详细信息
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<UserInfoVo> getUserInfo(HttpSession session) {
        // 检验用户是否登录
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // 获取用户信息
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return HandlerResult.success(userInfoVo);
    }

    /**
     * 获取密保问题
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> forgetGetQuestion(String username) {
        return iUserService.forgetGetQuestion(username);
    }

    /**
     * 校验密保问题是否正确
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> forgetCheckAnswer(String username,
                                                   String question, String answer) {
        return iUserService.forgetCheckAnswer(username, question, answer);
    }

    /**
     * 忘记密码中的重置密码
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> forgetResetPassword(String username,
                                                     String passwordNew, String forgetToken) {

        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 登录状态中的重置密码
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> resetPassword(HttpSession session,
                                               String passwordOld, String passwordNew) {
        // 检验用户是否登录
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // 获取用户信息
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return iUserService.resetPassword(userInfoVo, passwordOld, passwordNew);
    }

    /**
     * 登录状态下更新个人信息
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<User> updateInformation(HttpSession session, User user) {
        // 检验用户是否登录
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // 获取用户信息
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        // 补全信息
        user.setId(userInfoVo.getId());
        // 更新
        HandlerResult<User> resultUser = iUserService.updateInformation(user);
        if (!resultUser.isSuccess()) {
            return resultUser;
        }
        session.setAttribute(Const.CURRENT_USER, resultUser);
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    /**
     * 获取用户信息，如果没有登录则强制登录
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<User> getInformation(HttpSession session) {
        // 检验用户是否登录
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // 获取用户信息
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return iUserService.getInformation(userInfoVo.getId());
    }
}
