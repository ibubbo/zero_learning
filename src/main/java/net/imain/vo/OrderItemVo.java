package net.imain.vo;

import java.math.BigDecimal;

/**
 * @author: uncle
 * @apdateTime: 2017-12-07 13:19
 */
public class OrderItemVo {

    /** 订单号 .*/
    private Long orderNo;

    /** 商品ID .*/
    private Integer productId;

    /** 商品名字 .*/
    private String productName;

    /** 商品图片 .*/
    private String productImage;

    /** 生成订单时的商品单价,单位是元,保留两位小数 .*/
    private BigDecimal currentUnitPrice;

    /** 商品数量 .*/
    private Integer quantity;

    /** 商品总价,单位是元,保留两位小数 .*/
    private BigDecimal totalPrice;

    /** 创建时间 .*/
    private String createTime;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getCurrentUnitPrice() {
        return currentUnitPrice;
    }

    public void setCurrentUnitPrice(BigDecimal currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
