package net.imain.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import net.imain.common.Constants;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.service.OrderService;
import net.imain.vo.UserInfoVo;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: uncle
 * @apdateTime: 2017-12-04 17:03
 */
@RestController
@RequestMapping("/order/")
public class OrderController {

    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;


    /**
     * 预下单
     *
     * @param orderNo 订单号
     * @param request 获取上下文
     * @return 成功失败
     */
    @RequestMapping(value = "pay.do", method = RequestMethod.GET)
    public HandlerResult pay(Long orderNo, HttpServletRequest request) {
        // c://..../upload
        String path = request.getSession().getServletContext().getRealPath("upload");
        String imgPath = "/qr" + new DateTime().toString("/yyyy/MM/dd/");
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");

        return orderService.pay(userInfoVo.getId(), orderNo, path, imgPath);
    }

    /**
     * 支付宝回调接口
     * todo 此方法是否真的会被调用两次
     * @param request 回调信息存放的容器
     * @return
     */
    @RequestMapping(value = "alipay_callback.do")
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> requestParameters = request.getParameterMap();
        for (Iterator iterator = requestParameters.keySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = requestParameters.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        log.info("支付宝回调，sing：{}，trade_status：{}，参数：{}",
                requestParameters.get("sign"), requestParameters.get("trade_status"), params.toString());
        /**
         * 非常非常非常重要：
         *  1.验证回调的正确性
         *  2.验证是否是支付宝发出的
         *  3.通知排重
         */
        // 源码中没有移除sign_type，我们自己移除
        params.remove("sign_type");
        try {
            /**
             * 1.从参数列表中除去sign参数
             * 2.将剩下参数进行url_decode, 然后进行字典排序，组成字符串，得到待签名字符串
             * 3.将签名参数（sign）使用base64解码为字节码串。
             * 4.使用RSA2的验签方法，通过签名字符串、签名参数（经过base64解码）及支付宝公钥验证签名。
             */
            boolean alipayRSACheckedV2 =
                    AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRSACheckedV2) {
                return HandlerResult.error("非法请求，验证不通过，别调皮好吗");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常：{}", e);
        }
        //
        HandlerResult aliCallback = orderService.aliCallback(params);
        if (aliCallback.isSuccess()) {
            return Constants.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Constants.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping(value = "query_order_pay_status.do")
    public HandlerResult<Boolean> queryOrderPayStatus(Long orderNo, HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        HandlerResult<Boolean> queryOrderPayStatus = orderService.queryOrderPayStatus(userInfoVo.getId(), orderNo);
        if (queryOrderPayStatus.isSuccess()) {
            return HandlerResult.success(true);
        }
        return HandlerResult.success(false);
    }

    @RequestMapping(value = "create.do")
    public HandlerResult create(Integer shippingId, HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return orderService.createOrder(userInfoVo.getId(), shippingId);
    }

    @RequestMapping("cancel.do")
    public HandlerResult<String> cancel(Long orderNo, HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return orderService.cancel(userInfoVo.getId(), orderNo);
    }

    @RequestMapping("get_order_cart_product.do")
    public HandlerResult<String> getOrderCartProduct(HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return orderService.getOrderCartProduct(userInfoVo.getId());
    }

    @RequestMapping("detail.do")
    public HandlerResult<String> detail(Long orderNo,HttpServletRequest request) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return orderService.detail(userInfoVo.getId(), orderNo);
    }

    @RequestMapping("list.do")
    public HandlerResult<PageInfo> list(HttpServletRequest request,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        UserInfoVo userInfoVo = (UserInfoVo) request.getAttribute("userInfo");
        return orderService.list(userInfoVo.getId(), pageSize, pageNum);
    }
}
