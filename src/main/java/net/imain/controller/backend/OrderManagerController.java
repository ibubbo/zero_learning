package net.imain.controller.backend;

import com.github.pagehelper.PageInfo;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.service.OrderService;
import net.imain.service.UserService;
import net.imain.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author: uncle
 * @apdateTime: 2017-12-07 17:48
 */
@RestController
@RequestMapping("/manage/order/")
public class OrderManagerController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("list.do")
    public HandlerResult<PageInfo> list(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return orderService.manageList(pageSize, pageNum);
    }

    @RequestMapping("detail.do")
    public HandlerResult<OrderVo> detail(Long orderNo) {
        return orderService.managerDetail(orderNo);
    }

    @RequestMapping("search.do")
    public HandlerResult<PageInfo> search(Long orderNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return orderService.managerSearch(orderNo, pageSize, pageNum);
    }

    @RequestMapping("send_goods.do")
    public HandlerResult<String> sendGoods(Long orderNo) {
        return orderService.sendGoods(orderNo);
    }
}
