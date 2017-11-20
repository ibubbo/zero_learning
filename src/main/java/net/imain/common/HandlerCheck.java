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
     */
    public static HandlerResult checkUserIsPresentAndRole(HttpSession session, IUserService iUserService) {
        // 判断用户是否登录
        HandlerResult<UserInfoVo> resultUser = checkUserIsPresent(session, true);
        if (!resultUser.isSuccess()) {
            return resultUser;
        }
        // 校验用户是否是管理员
        HandlerResult userRole = checkUserRole(iUserService, resultUser.getData());
        if (!userRole.isSuccess()) {
            return userRole;
        }
        return userRole;
    }

    /**
     * 检查用户权限
     *
     * @param iUserService 用户服务对象
     * @param userInfoVo 用户信息
     * @return
     */
    public static HandlerResult<String> checkUserRole(IUserService iUserService, UserInfoVo userInfoVo) {
        // 校验用户是否是管理员
        HandlerResult adminRole = iUserService.checkAdminRole(userInfoVo);
        if (!adminRole.isSuccess()) {
            return HandlerResult.error(UserEnum.IS_NOT_ADMIN.getMessage());
        }
        // 返回校验成功
        return HandlerResult.success();
    }

    /**
     * 检查用户信息
     *
     * @param session 用户信息
     * @param flag 是否需要返回
     * @return
     */
    public static HandlerResult checkUserIsPresent(HttpSession session, boolean flag) {
        // 判断用户是否登录
        Optional<UserInfoVo> userInfoVo =
                Optional.ofNullable(((UserInfoVo) session.getAttribute(Const.CURRENT_USER)));
        if (!userInfoVo.isPresent()) {
            return HandlerResult.error(UserEnum.NEED_LOGIN.getCode(), UserEnum.NEED_LOGIN.getMessage());
        }
        if (flag) {
            return HandlerResult.success(userInfoVo.get());
        }
        return HandlerResult.success();
    }
}
