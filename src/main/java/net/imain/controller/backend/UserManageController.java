package net.imain.controller.backend;

import net.imain.common.Constants;
import net.imain.common.HandlerResult;
import net.imain.enums.UserEnum;
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
 * @apdateTime: 2017-11-18 15:49
 */
@Controller
@RequestMapping(value = "/manage/user/")
public class UserManageController {

    @Autowired
    private UserService userService;

    /**
     * 管理员登录
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public HandlerResult<UserInfoVo> login(String username,
                                           String password, HttpSession session) {
        HandlerResult<UserInfoVo> userInfo = userService.login(username, password);
        if (userInfo.isSuccess()) {
            UserInfoVo user = userInfo.getData();
            if (!(user.getRole() == Constants.Role.ROLE_ADMIN)) {
                return HandlerResult.error(UserEnum.IS_NOT_ADMIN.getMessage());
            }
            // 说明登录的是管理员
            session.setAttribute(Constants.CURRENT_USER, user);
        }
        return userInfo;
    }
}
