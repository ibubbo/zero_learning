package net.imain.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.dao.OrderItemMapper;
import net.imain.dao.OrderMapper;
import net.imain.enums.OrderEnum;
import net.imain.pojo.Order;
import net.imain.pojo.OrderItem;
import net.imain.service.OrderService;
import net.imain.util.BigDecimalUtil;
import net.imain.util.FTPUtil;
import net.imain.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: uncle
 * @apdateTime: 2017-12-04 17:08
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public HandlerResult pay(Integer userId, Long orderNo, String path, String imgPath) {
        // 数据校验
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            return HandlerResult.success(OrderEnum.USER_HAS_NO_ORDER.getMessage());
        }
        // 拼接响应格式的容器
        Map<String, String> resultMap = Maps.newHashMap();
        // 拼接格式一
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        // ↓ 拼接格式二开始------------------------------------------------

        // (必填) 订单系统中唯一订单号
        String outTradeNo = String.valueOf(order.getOrderNo());

        // 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder()
                .append("开心购商城扫码支付，订单号：")
                .append(outTradeNo).toString();

        // (必填) 订单总金额(元)
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单 【")
                .append(outTradeNo)
                .append("】购买商品共")
                .append(totalAmount)
                .append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计。这里使用默认的
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持。这里也使用默认的
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持。这里也使用默认的
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟(默认)
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<>();

        // TODO 这里只要根据用户ID查还是需要跟订单号一起查？如果这里是用userId和orderNo一起查的话，是不可能有一个列表存在的
        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId, orderNo);
        for (OrderItem orderItem : orderItemList) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(),
                    orderItem.getProductName(),
                    BigDecimalUtil.multiply(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                // 回调地址
                .setNotifyUrl(PropertiesUtil.getProperties("alipay.callback.url"))
                .setGoodsDetailList(goodsDetailList);

        /**
         *  加载properties文件，一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         */
        Configs.init("zfbinfo.properties");

        /**
         *  创建请求对象，使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         *
         *  AlipayClient alipayClient =
         *      new DefaultAlipayClient("支付宝网关","APPID","私钥","json","utf-8","公钥","RSA2");
         */
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        // 正式发起请求
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功");

                // 得到支付宝的响应对象
                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 创建二维码存放的工程路径
                File file = new File(path);
                if (!file.exists()) {
                    // 赋予写权限
                    file.setWritable(true);
                    // 创建目录
                    file.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());

                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                log.info("二维码的路径和名字：{}, {}", qrPath, qrFileName);
                File targetFile = new File(path, qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile), imgPath);
                } catch (IOException e) {
                    log.error("上传二维码异常: {}", e);
                }

                log.info("filePath:" + qrPath);

                String qrUrl = PropertiesUtil.getProperties("ftp.server.http.prefix") + imgPath + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);

                // ↑ 拼接格式二结束------------------------------------------------

                log.info("图片最后的路径：{}", qrUrl);
                return HandlerResult.success(resultMap);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return HandlerResult.error("支付宝预下单失败");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return HandlerResult.error("系统异常，预下单状态未知");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return HandlerResult.error("不支持的交易状态，交易返回异常");
        }
    }

    /**
     * 简单打印应答
     *
     * @param response 支付响应对象
     */
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
}
