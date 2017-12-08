package net.imain.interceptor.portal;

import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.util.GsonUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前台拦截器
 *
 * @author: uncle
 * @apdateTime: 2017-12-08 14:32
 */
public class PortalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        httpServletResponse.setContentType("application/json");

        HandlerResult handlerResult =
                HandlerCheck.checkUserIsPresent(httpServletRequest.getSession());
        if (!handlerResult.isSuccess()) {
            String json = GsonUtil.getGSON().toJson(handlerResult);
            httpServletResponse.getWriter().print(json);
            return false;
        }
        httpServletRequest.setAttribute("userInfo", handlerResult.getData());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
