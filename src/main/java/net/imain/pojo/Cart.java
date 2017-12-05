package net.imain.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 购物车
 *
 * @author uncle
 */
public class Cart {

    /** 购物车ID .*/
    private Integer id;

    /** 用户ID .*/
    private Integer userId;

    /** 商品ID .*/
    private Integer productId;

    /** 商品数量 .*/
    private Integer quantity;

    /** 是否是选中状态 1 选中  2 未选中 .*/
    private Integer checked;

    /** 创建时间 .*/
    private Date createTime;

    /** 更新时间 .*/
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Cart() {
    }

    public Cart(Integer id, Integer userId, Integer productId, Integer quantity, Integer checked, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.checked = checked;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}