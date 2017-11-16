package net.imain.controller.portal;

import net.imain.common.Const;
import net.imain.common.ServerResponse;
import net.imain.pojo.User;
import net.imain.service.IUserService;
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
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username,
                                      String password, HttpSession session) {
        ServerResponse<User> user = iUserService.login(username, password);
        if (user.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, user.getData());
        }
        throw new NullPointerException("腾讯大礼包...");
//        return user;
    }
}
