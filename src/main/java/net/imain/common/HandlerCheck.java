package net.imain.common;

import net.imain.enums.UserEnum;
import net.imain.service.IUserService;
import net.imain.vo.UserInfoVo;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * 全局校验类
 *
 * @author: uncle
 * @apdateTime: 2017-11-19 14:21
 */
public class HandlerCheck {
    /**
     * 用户登录和权限判断
     *
     * @param session 用户的session信息
     * @param iUserService 用户权限认证方法
     * @return
     */
    public static HandlerResult checkNullAndRole(HttpSession session, IUserService iUserService) {
        // 1.判断用户是否登录
        Optional<UserInfoVo> userInfoVo =
                Optional.ofNullable(((UserInfoVo) session.getAttribute(Const.CURRENT_USER)));
        if (!userInfoVo.isPresent()) {
            return HandlerResult.error(UserEnum.NEED_LOGIN.getCode(), UserEnum.NEED_LOGIN.getMessage());
        }
        // 2.判断用户是否是管理员
        HandlerResult adminRole = iUserService.checkAdminRole(userInfoVo.get());
        if (!adminRole.isSuccess()) {
            return HandlerResult.error(UserEnum.IS_NOT_ADMIN.getMessage());
        }
        return HandlerResult.success();
    }
}
