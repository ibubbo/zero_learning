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

import javax.servlet.http.HttpServletRequest;
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
    public HandlerResult add(Integer productId, Integer count, HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.saveOrUpdate(userInfoVo.getId(), productId, count);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult list(HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.list(userInfoVo.getId());
    }

    @RequestMapping(value = "update.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult update(HttpServletRequest request, Integer productId, Integer count) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.update(userInfoVo.getId(), productId, count);
    }

    @RequestMapping(value = "delete_product.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult delete(HttpServletRequest request, String productIds) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.delete(userInfoVo.getId(), productIds);
    }

    // 单选
    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult select(HttpServletRequest request, Integer productId) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.selectOrUnSelect(userInfoVo.getId(), productId, true);
    }

    @RequestMapping(value = "un_select.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult unSelect(HttpServletRequest request, Integer productId) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.selectOrUnSelect(userInfoVo.getId(), productId, false);
    }

    // 全选
    @RequestMapping(value = "select_all.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult selectAll(HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.selectAllOrUnSelectAll(userInfoVo.getId(), true);
    }

    @RequestMapping(value = "un_select_all.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult unSelectAll(HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.selectAllOrUnSelectAll(userInfoVo.getId(), false);
    }

    @RequestMapping(value = "get_cart_product_count.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult getCartProductCount(HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return cartService.getCartProductCount(userInfoVo.getId());
    }
}
