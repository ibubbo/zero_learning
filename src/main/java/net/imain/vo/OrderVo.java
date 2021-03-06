package net.imain.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: uncle
 * @apdateTime: 2017-12-07 13:15
 */
public class OrderVo {
    /**
     * 订单号 .
     */
    private Long orderNo;

    /**
     * 实际付款金额,单位是元,保留两位小数 .
     */
    private BigDecimal payment;

    /**
     * 支付类型,1-在线支付 .
     */
    private Integer paymentType;

    /**
     * 支付描述 .
     */
    private String paymentTypeDesc;

    /**
     * 运费,单位是元 .
     */
    private Integer postage;

    /**
     * 订单状态:0-已取消，10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭 .
     */
    private Integer status;

    /**
     * 状态描述 .
     */
    private String statusDesc;

    /**
     * 支付时间 .
     */
    private String paymentTime;

    /**
     * 发货时间 .
     */
    private String sendTime;

    /**
     * 交易完成时间 .
     */
    private String endTime;

    /**
     * 交易关闭时间 .
     */
    private String closeTime;

    /**
     * 创建时间 .
     */
    private String createTime;

    /**
     * 订单明细 .
     */
    private List<OrderItemVo> orderItemVoList;

    /**
     * 图片地址 .
     */
    private String imageHost;

    /**
     * 收货地址ID .
     */
    private Integer shippingId;

    /**
     * 收货人名称 .
     */
    private String receiverName;

    /**
     * 收货人地址详情 .
     */
    private ShippingVo shippingVo;

    public String getImageHost() {
        return imageHost;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public ShippingVo getShippingVo() {
        return shippingVo;
    }

    public void setShippingVo(ShippingVo shippingVo) {
        this.shippingVo = shippingVo;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentTypeDesc() {
        return paymentTypeDesc;
    }

    public void setPaymentTypeDesc(String paymentTypeDesc) {
        this.paymentTypeDesc = paymentTypeDesc;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }
}
