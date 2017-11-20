package net.imain.controller.backend;

import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.pojo.Product;
import net.imain.service.ProductService;
import net.imain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 商品管理
 *
 * @author: uncle
 * @apdateTime: 2017-11-20 11:50
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * 更新商品
     */
    @RequestMapping(value = "save.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> save(HttpSession session, Product product) {
        // 校验用户和权限
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // 更新商品并返回
        return productService.saveOrUpdate(product);
    }

    /**
     * 商品上下架
     */
    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> setSaleStatus(HttpSession session, Integer productId, Integer status) {
        // 校验用户和权限
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        return productService.setSaleStatus(productId, status);
    }

    /**
     * 商品详情
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<Product> getDetail(HttpSession session, Integer productId) {
        // 校验用户和权限
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }

        // 查询商品详情
        return null;
    }

}
