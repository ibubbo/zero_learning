package net.imain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: uncle
 * @apdateTime: 2017-11-23 10:24
 */
@Data
public class CartProductVo<T> {

    /** 购物车ID .*/
    private Integer id;

    /** 用户ID .*/
    private Integer userId;

    /** 商品ID .*/
    private Integer productId;

    /** 商品总数 .*/
    private Integer quantity;

    /** 商品名称 .*/
    private String productName;

    /** 商品副标题 .*/
    private String productSubtitle;

    /** 商品主题 .*/
    private String productMainImage;

    /** 商品价格 .*/
    private BigDecimal productPrice;

    /** 商品状态 .*/
    private Integer productStatus;

    /** 商品总价 .*/
    private BigDecimal productTotalPrice;

    /** 商品库存 .*/
    private Integer productStock;

    /** 商品状态 1 选中， 2 未选中 .*/
    private Integer productChecked;

    /** 限制数量的一个返回结果 .*/
    private String limitQuantity;

}
