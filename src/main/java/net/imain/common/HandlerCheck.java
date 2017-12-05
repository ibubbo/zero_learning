package net.imain.common;

import net.imain.enums.UserEnum;
import net.imain.service.UserService;
import net.imain.vo.UserInfoVo;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Collection;
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
    public static HandlerResult checkUserIsPresentAndRole(HttpSession session, UserService userService) {
        // 判断用户是否登录
        HandlerResult<UserInfoVo> resultUser = checkUserIsPresent(session);
        if (!resultUser.isSuccess()) {
            return resultUser;
        }
        // 校验用户是否是管理员
        HandlerResult userRole = checkUserRole(userService, resultUser.getData());
        if (!userRole.isSuccess()) {
            return userRole;
        }
        return userRole;
    }

    /**
     * 检查用户权限
     *
     * @param userService 用户服务对象
     * @param userInfoVo  用户信息
     * @return
     */
    public static HandlerResult<String> checkUserRole(UserService userService, UserInfoVo userInfoVo) {
        // 校验用户是否是管理员
        HandlerResult adminRole = userService.checkAdminRole(userInfoVo);
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
     * @return
     */
    public static HandlerResult checkUserIsPresent(HttpSession session) {
        // 判断用户是否登录
        Optional<UserInfoVo> userInfoVo =
                Optional.ofNullable(((UserInfoVo) session.getAttribute(Constants.CURRENT_USER)));
        if (!userInfoVo.isPresent()) {
            return HandlerResult.error(UserEnum.NEED_LOGIN.getCode(), UserEnum.NEED_LOGIN.getMessage());
        }
        return HandlerResult.success(userInfoVo.get());
    }

    /**
     * Numerical Judgment
     *
     * @param bean param
     * @param <N>  Numerical type
     * @return true si null. false not is null
     */
    public static <N> boolean NumIsEmpty(N bean) {
        return !(Optional.ofNullable(bean).isPresent());
    }

    public static <N> boolean NumIsNotEmpty(N bean) {
        return Optional.ofNullable(bean).isPresent();
    }

    /**
     * Object Judgment
     *
     * @param bean
     * @param <T>
     * @return true si null. false not is null
     */
    public static <T> boolean ObjectIsEmpty(T bean) {
        Optional<T> optional = Optional.ofNullable(bean);
        if (!optional.isPresent() || "".equals(bean)) {
            return true;
        }
        if (bean instanceof Collection) {
            if (((Collection) bean).size() <= 0) {
                return true;
            }
            return false;
        }
        Class<?> aClass = bean.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            String getMethod = method.getName().substring(0, 3);
            if ("get".equals(getMethod)) {
                try {
                    Method aClassMethod = aClass.getMethod(method.getName());
                    Object invoke = aClassMethod.invoke(bean);
                    if (invoke != null) {
                        if (!"".equals(invoke)) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return true;
    }

    public static <T> boolean ObjectIsNotEmpty(T bean) {
        return !(ObjectIsEmpty(bean));
    }
}
