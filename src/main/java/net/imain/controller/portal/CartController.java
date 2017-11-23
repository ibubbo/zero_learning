package net.imain.controller.portal;

import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.service.CartService;
import net.imain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: uncle
 * @apdateTime: 2017-11-23 10:19
 */
@Controller
@RequestMapping(value = "/cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "add.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult add(Integer productId, Integer count, HttpSession session) {
        // check user is null
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return cartService.add(productId, count, userInfoVo.getId());
    }
}
