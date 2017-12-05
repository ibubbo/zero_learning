package net.imain.controller.portal;

import com.github.pagehelper.PageInfo;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.pojo.Shipping;
import net.imain.service.ShippingService;
import net.imain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author: uncle
 * @apdateTime: 2017-12-03 10:19
 */
@RestController
@RequestMapping(value = "/shipping/")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @RequestMapping(value = "add.do", method = RequestMethod.GET)
    public HandlerResult add(Shipping shipping, HttpSession session) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return shippingService.saveOrUpdate(userInfoVo.getId(), shipping);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.GET)
    public HandlerResult update(Shipping shipping, HttpSession session) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return shippingService.saveOrUpdate(userInfoVo.getId(), shipping);
    }

    @RequestMapping(value = "del.do", method = RequestMethod.GET)
    public HandlerResult<String> del(HttpSession session, Integer shippingId) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return shippingService.del(userInfoVo.getId(), shippingId);
    }

    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    public HandlerResult<Shipping> select(HttpSession session, Integer shippingId) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return shippingService.select(userInfoVo.getId(), shippingId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    public HandlerResult<PageInfo> list(HttpSession session,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresent(session);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        UserInfoVo userInfoVo = (UserInfoVo) handlerResult.getData();
        return shippingService.list(userInfoVo.getId(), pageNum, pageSize);
    }
}
