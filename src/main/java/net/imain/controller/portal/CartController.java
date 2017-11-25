package net.imain.controller.portal;

import com.google.common.collect.Lists;
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
import java.util.List;

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
        return cartService.saveOrUpdate(userInfoVo.getId(), productId, count);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult list(HttpSession session) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return cartService.list(userInfoVo.getId());
    }

    @RequestMapping(value = "update.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult update(HttpSession session, Integer productId, Integer count) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return cartService.update(userInfoVo.getId(), productId, count);
    }

    @RequestMapping(value = "delete_product.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult delete(HttpSession session, String productIds) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return cartService.delete(userInfoVo.getId(), productIds);
    }

    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult updateProductChecked(HttpSession session, String productId) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return null;
    }
}
