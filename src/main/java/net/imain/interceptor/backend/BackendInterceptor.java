package net.imain.interceptor.backend;

import com.google.common.collect.Maps;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.service.UserService;
import net.imain.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 管理员功能拦截器
 *
 * @author: uncle
 * @apdateTime: 2017-12-08 10:37
 */
public class BackendInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    /**
     * Action之前执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception {
        HandlerResult resultCheck =
                HandlerCheck.checkUserIsPresentAndRole(httpServletRequest.getSession(), userService);
        if (!resultCheck.isSuccess()) {
            // 权限不足

            httpServletResponse.setContentType("application/json");

            String name = ((HandlerMethod) o).getMethod().getName();
            if ("richtextImgUpload".equals(name)) {
                // 富文本上传，特殊格式
                Map map = Maps.newHashMap();
                map.put("success", false);
                map.put("msg", resultCheck.getMsg());
                String json = GsonUtil.getGSON().toJson(map);
                httpServletResponse.getWriter().print(json);
                return false;
            }
            // 普通格式
            String json = GsonUtil.getGSON().toJson(resultCheck);
            httpServletResponse.getWriter().print(json);
            return false;
        }
        return true;
    }

    /**
     * 生成视图之前执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 最后执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
