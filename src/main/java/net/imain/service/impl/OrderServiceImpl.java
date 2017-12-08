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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.imain.common.Constants;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerConverter;
import net.imain.common.HandlerResult;
import net.imain.dao.*;
import net.imain.enums.HandlerEnum;
import net.imain.enums.OrderEnum;
import net.imain.enums.ProductEnum;
import net.imain.pojo.*;
import net.imain.service.OrderService;
import net.imain.util.BigDecimalUtil;
import net.imain.util.FTPUtil;
import net.imain.util.PropertiesUtil;
import net.imain.vo.OrderItemVo;
import net.imain.vo.OrderProductVo;
import net.imain.vo.OrderVo;
import net.imain.vo.ShippingVo;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 在做这个的时候有个错，时间格式不对。。
 *
 * @author: uncle
 * @apdateTime: 2017-12-04 17:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShippingMapper shippingMapper;

    // todo 订单金额不能少于等于0元
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

                // 将支付宝返回的二维码链接内容传给Zxing
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

    @Override
    public HandlerResult aliCallback(Map<String, String> params) {
        // 订单号
        Long outTradeNo = Long.valueOf(params.get("out_trade_no"));
        // 支付宝交易号
        String tradeNo = params.get("trade_no");
        // 交易状态
        String tradeStatus = params.get("trade_status");

        Order order = orderMapper.selectByOrderNo(outTradeNo);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            // 订单不存在
            return HandlerResult.error("非快乐购商城的订单，回调忽略");
        }
        //
        /**
         * 订单状态:0-已取消，10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
         * PAID = 20
         */
        if (order.getStatus() >= Constants.OrderStatusEnum.PAID.getCode()) {
            // 订单状态码大于等于20说明已经交易过
            return HandlerResult.success("支付宝重复调用");
        }
        if (Constants.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            // 交易状态是成功的：1.更新交易时间，2.更新状态
            order.setPaymentTime(HandlerConverter.strToDate(params.get("gmt_payment")));
            order.setStatus(Constants.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        // 支付详情
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Constants.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);

        return HandlerResult.success();
    }

    /**
     * 查询订单状态
     */
    @Override
    public HandlerResult queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            return HandlerResult.error(OrderEnum.USER_HAS_NO_ORDER.getMessage());
        }
        // 判断支付状态
        if (order.getStatus() >= Constants.OrderStatusEnum.PAID.getCode()) {
            return HandlerResult.success();
        }
        return HandlerResult.error();
    }

    /**
     * 创建订单的一个步骤：
     * -------------------- 准备工作 ----------------------
     * 1.前端传了一个收货地址ID，这是条件一。
     * 2.通过session得到用户的ID，这是条件二。
     * 3.创建订单，用户在前端会选择自己的收货地址，并且订单准备结账的时候，只选择用户在购物车中选中的商品，没有选择就不需要结账
     * <p>
     * -------------------- 正式开发 ----------------------
     * 1.根据用户ID查询用户的购物车中选中的商品：select * from {table} where user_id = #{userId} and checked = {[1]|[2]|[...]}
     * 2.判断查询出来的购物车：
     * i.购物车中是否有数据
     * ii.如果有，判断商品库存是否足够
     * iii.判断商品状态是否合法
     * 3.拼接商品详细信息(为前端格式的orderItemVoList属性做准备)
     * 4.生成订单号：System.current.. + Random, 根据第三步的商品详情计算商品总价
     * 5.补全订单信息，并添加Order到数据库中，此时生成订单已完成
     * 6.补全商品详情的订单号，并添加到数据库中
     * 7.减库存（直接减，库存足不足已经在第二步判断过了）
     * 8.清空购物车(用户ID + 选中状态)
     * 8.拼接前端响应格式并返回
     *
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public HandlerResult createOrder(Integer userId, Integer shippingId) {

        // 根据用户ID从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        // 拼接内层数据
        HandlerResult<List<OrderItem>> cartOrderItem = this.getCartOrderItem(userId, cartList);
        if (!cartOrderItem.isSuccess()) {
            return cartOrderItem;
        }
        List<OrderItem> orderItemList = cartOrderItem.getData();

        if (HandlerCheck.ObjectIsEmpty(orderItemList)) {
            return HandlerResult.error("购物车为空");
        }

        // 计算订单总价
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);

        // 生成订单
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            return HandlerResult.error(OrderEnum.ORDER_GENERATION_ERROR.getMessage());
        }

        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }

        // 生成订单详情
        orderItemMapper.batchInsert(orderItemList);
        // 减库存
        this.reduceProductStock(orderItemList);
        // 清空购物车(根据用户的ID和商品的选中状态进行删除)
        cartMapper.deleteCartByUserId(userId);

        return HandlerResult.success(this.assembleOrderVo(order, orderItemList));
    }


    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        // todo vo 有一个 receiverName 没有设置
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Constants.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVo.setStatus(order.getStatus());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatusDesc(Constants.OrderStatusEnum.codeOf(order.getStatus()).getValue());
        orderVo.setShippingId(order.getShippingId());

        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        ShippingVo shippingVo = new ShippingVo();
        if (HandlerCheck.ObjectIsNotEmpty(shipping)) {
            shippingVo.setReceiverAddress(shipping.getReceiverAddress());
            shippingVo.setReceiverCity(shipping.getReceiverCity());
            shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVo.setReceiverMobile(shipping.getReceiverMobile());
            shippingVo.setReceiverName(shipping.getReceiverName());
            shippingVo.setReceiverPhone(shipping.getReceiverPhone());
            shippingVo.setReceiverProvince(shipping.getReceiverProvince());
            shippingVo.setReceiverZip(shipping.getReceiverZip());
        }

        orderVo.setShippingVo(shippingVo);

        orderVo.setPaymentTime(HandlerConverter.dateToString(order.getPaymentTime()));
        orderVo.setSendTime(HandlerConverter.dateToString(order.getSendTime()));
        orderVo.setEndTime(HandlerConverter.dateToString(order.getEndTime()));
        orderVo.setCreateTime(HandlerConverter.dateToString(order.getCreateTime()));
        orderVo.setCloseTime(HandlerConverter.dateToString(order.getCloseTime()));

        orderVo.setImageHost(PropertiesUtil.getProperties(Constants.Ftp.FTP_SERVER_HTTP_PREFIX_KEY));

        orderVo.setOrderItemVoList(this.orderItemToOrderItemVo(orderItemList));
        return orderVo;
    }

    /**
     * 订单详细转订单详细VO
     *
     * @param orderItemList 订单详细列表
     * @return
     */
    private List<OrderItemVo> orderItemToOrderItemVo(List<OrderItem> orderItemList) {
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = new OrderItemVo();
            orderItemVo.setOrderNo(orderItem.getOrderNo());
            orderItemVo.setProductId(orderItem.getProductId());
            orderItemVo.setProductName(orderItem.getProductName());
            orderItemVo.setProductImage(orderItem.getProductImage());
            orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVo.setQuantity(orderItem.getQuantity());
            orderItemVo.setTotalPrice(orderItem.getTotalPrice());
            orderItemVo.setCreateTime(HandlerConverter.dateToString(orderItem.getCreateTime()));
            orderItemVoList.add(orderItemVo);
        }
        return orderItemVoList;
    }

    /**
     * 减库存
     *
     * @param orderItemList 订单详情
     */
    private void reduceProductStock(List<OrderItem> orderItemList) {
        // todo 可以使用mybatis的upate批量更新，日后优化
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    /**
     * 拼接订单信息
     *
     * @param userId     用户ID
     * @param shippingId 收货地址ID
     * @param payment    商品总价
     * @return 订单对象
     */
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();

        order.setOrderNo(this.generateOrderNo());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(Constants.PaymentTypeEnum.ONELINE_PAY.getCode());
        order.setStatus(Constants.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);

        int resultInsertSum = orderMapper.insert(order);
        if (resultInsertSum == 0) {
            return null;
        }
        return order;
    }

    /**
     * 生成订单号
     *
     * @return 订单号
     */
    private long generateOrderNo() {
        // 如何设计订单号，为处理高并发、集群、多数据源、分库分表做准备
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /**
     * 计算订单总价
     *
     * @param orderItemList 订单对象
     * @return 总价
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal orderTotal = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            // bug，add 方法不会对传入的参数有所改变，所以需要拿值来接收从而达到更新的目的
            orderTotal = BigDecimalUtil.add(orderTotal.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return orderTotal;
    }

    /**
     * 拼接内层格式数据
     *
     * @param userId
     * @param cartList
     * @return
     */
    private HandlerResult<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (HandlerCheck.ObjectIsEmpty(cartList)) {
            return HandlerResult.error("购物车为空");
        }
        // 校验购物车的数据
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            // 校验状态
            if (Constants.ProductStatusEnum.SALE.getStatus() != product.getStatus()) {
                // 产品下线
                return HandlerResult.error(ProductEnum.PRODUCT_HAS_REMOVED.getMessage());
            }
            // 库存
            if (product.getStock() < cartItem.getQuantity()) {
                // 库存不足
                return HandlerResult.error(product.getName() + "商品库存不足");
            }
            // 补全orderItem信息
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.multiply(cartItem.getQuantity(), product.getPrice().doubleValue()));

            orderItemList.add(orderItem);
        }
        return HandlerResult.success(orderItemList);
    }

    @Override
    public HandlerResult<String> cancel(Integer userId, Long orderNo) {
        // 数据校验
        if (HandlerCheck.NumIsEmpty(orderNo)) {
            return HandlerResult.success(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            return HandlerResult.error(OrderEnum.USER_HAS_NO_ORDER.getMessage());
        }
        if (order.getStatus() >= Constants.OrderStatusEnum.PAID.getCode()) {
            return HandlerResult.error("订单已付款，无法取消");
        }

        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Constants.OrderStatusEnum.CANSELED.getCode());
        int updateByPrimaryKeySelective = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (updateByPrimaryKeySelective == 0) {
            return HandlerResult.error();
        }
        return HandlerResult.success();
    }

    @Override
    public HandlerResult getOrderCartProduct(Integer userId) {
        List<OrderItem> orderItemList = orderItemMapper.selectByUserId(userId);
        if (HandlerCheck.ObjectIsEmpty(orderItemList)) {
            return HandlerResult.success(orderItemList);
        }
        OrderProductVo productVo = new OrderProductVo();
        productVo.setImageHost(PropertiesUtil.getProperties(Constants.Ftp.FTP_SERVER_HTTP_PREFIX_KEY));
        productVo.setOrderItemVoList(this.orderItemToOrderItemVo(orderItemList));
        productVo.setProductTotalPrice(this.getOrderTotalPrice(orderItemList));

        return HandlerResult.success(productVo);
    }

    @Override
    public HandlerResult detail(Integer userId, Long orderNo) {
        if (HandlerCheck.NumIsEmpty(orderNo)) {
            return HandlerResult.success(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            return HandlerResult.error("没有找到该订单");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId, orderNo);
        // 拼接格式
        return HandlerResult.success(this.assembleOrderVo(order, orderItemList));
    }

    @Override
    public HandlerResult<PageInfo> list(Integer userId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderVo> orderVoList = this.orderToOrderVo(orderMapper.selectByUserId(userId), userId);
        return HandlerResult.success(new PageInfo(orderVoList));
    }

    /**
     * 类型转换(list)
     *
     * @return
     */
    private List<OrderVo> orderToOrderVo(List<Order> orderList, Integer userId) {
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList;
            if (HandlerCheck.ObjectIsEmpty(userId)) {
                // TODO 管理员查询不需要USERID
                orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId, order.getOrderNo());
            }
            orderVoList.add(this.assembleOrderVo(order, orderItemList));
        }
        return orderVoList;
    }

    @Override
    public HandlerResult<PageInfo> manageList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAll();
        List<OrderVo> orderVoList = this.orderToOrderVo(orderList, null);
        return HandlerResult.success(new PageInfo(orderVoList));
    }

    @Override
    public HandlerResult<OrderVo> managerDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            return HandlerResult.success("订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        return HandlerResult.success(this.assembleOrderVo(order, orderItemList));
    }

    /**
     * 暂时是精确查找
     *
     * @param orderNo 订单号
     * @return
     */
    @Override
    public HandlerResult<PageInfo> managerSearch(Long orderNo, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (HandlerCheck.ObjectIsEmpty(order)) {
            return HandlerResult.success("订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
        PageInfo pageInfo = new PageInfo(Lists.newArrayList(orderVo));
        return HandlerResult.success(pageInfo);
    }

    @Override
    public HandlerResult<String> sendGoods(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (HandlerCheck.ObjectIsNotEmpty(order)) {
            // 订单不为空
            if (order.getStatus() == Constants.OrderStatusEnum.PAID.getCode()) {
                // 状态是支付则更新信息
                order.setId(order.getId());
                order.setStatus(Constants.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                int updateByPrimaryKeySelective = orderMapper.updateByPrimaryKeySelective(order);
                if (updateByPrimaryKeySelective == 0) {
                    return HandlerResult.error("发货失败");
                }
                return HandlerResult.error("发货成功");
            }
            return HandlerResult.success("订单状态异常");
        }
        return HandlerResult.success("订单不存在");
    }
}
