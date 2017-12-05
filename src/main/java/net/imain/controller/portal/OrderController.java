package net.imain.controller.portal;

import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.service.OrderService;
import net.imain.vo.UserInfoVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: uncle
 * @apdateTime: 2017-12-04 17:03
 */
@RestController
@RequestMapping("/order/")
public class OrderController {


    @Autowired
    private OrderService orderService;

    /**
     * 订单接口
     *
     * @param session 用户信息
     * @param orderNo 订单号
     * @param request 获取上下文
     * @return 成功失败
     */
    @RequestMapping(value = "pay.do", method = RequestMethod.GET)
    public HandlerResult pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // c://..../upload
        String path = request.getSession().getServletContext().getRealPath("upload");
        String imgPath = "/qr" + new DateTime().toString("/yyyy/MM/dd/");
        System.out.println("用户请求路径：" + path);
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();

        return orderService.pay(userInfoVo.getId(), orderNo, path, imgPath);
    }
}
