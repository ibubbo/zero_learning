package net.imain.controller.portal;

import net.imain.common.Const;
import net.imain.common.ServerResponse;
import net.imain.pojo.User;
import net.imain.service.IUserService;
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
    private IUserService iUserService;

    /**
     * 用户登录
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<UserInfoVo> login(String username,
                                      String password, HttpSession session) {
        ServerResponse<UserInfoVo> user = iUserService.login(username, password);
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
    public ServerResponse<String> logout(HttpSession session) {
        try {
            session.removeAttribute(Const.CURRENT_USER);
        } catch (Exception e) {
            return ServerResponse.error("服务端异常");
        }
        return ServerResponse.success("退出成功");
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "register.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 校验用户名和邮箱
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户的详细信息
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<UserInfoVo> getUserInfo(HttpSession session) {
        UserInfoVo user = (UserInfoVo) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error("用户未登录,无法获取当前用户信息");
        }
        return ServerResponse.success(user);
    }

    /**
     * 获取密保问题
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.forgetGetQuestion(username);
    }

    /**
     * 校验密保问题是否正确
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,
                                                    String question, String answer) {
        return iUserService.forgetCheckAnswer(username, question, answer);
    }
}
