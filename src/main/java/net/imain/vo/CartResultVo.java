package net.imain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: uncle
 * @apdateTime: 2017-11-23 11:27
 */
@Data
public class CartResultVo {
    /** 购物车列表 .*/
    private List<CartProductVo> cartProductVoList;

    /** 购物车商品总价 .*/
    private BigDecimal cartTotalPrice;

    /** 是否勾选 .*/
    private boolean allChecked;

    /** 图片地址 .*/
    private String imageHost;
}
