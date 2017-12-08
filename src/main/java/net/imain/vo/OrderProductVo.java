package net.imain.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: uncle
 * @apdateTime: 2017-12-07 16:20
 */
public class OrderProductVo {

    /**
     * 订单明细 .
     */
    private List<OrderItemVo> orderItemVoList;

    /**
     * 图片地址 .
     */
    private String imageHost;

    /**
     * 订单总价
     */
    BigDecimal productTotalPrice;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
