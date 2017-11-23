package net.imain.service.impl;

import com.google.common.collect.Lists;
import net.imain.common.Const;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.dao.CartMapper;
import net.imain.dao.ProductMapper;
import net.imain.enums.HandlerEnum;
import net.imain.enums.ProductEnum;
import net.imain.pojo.Cart;
import net.imain.pojo.Product;
import net.imain.pojo.User;
import net.imain.service.CartService;
import net.imain.util.BigDecimalUtil;
import net.imain.util.PropertiesUtil;
import net.imain.vo.CartProductVo;
import net.imain.vo.CartResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: uncle
 * @apdateTime: 2017-11-23 10:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public HandlerResult<CartResultVo> add(Integer productId, Integer count, Integer userId) {
        // 校验数据
        if (HandlerCheck.NumIsEmpty(count) || HandlerCheck.NumIsEmpty(productId)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }

        // 校验商品ID是否合法
        Product product = productMapper.selectByPrimaryKey(productId);
        if (HandlerCheck.ObjectIsEmpty(product)) {
            return HandlerResult.success(ProductEnum.PRODUCT_NOT_EXIST.getMessage());
        }

        // 查询购物车
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (HandlerCheck.ObjectIsEmpty(cart)) {
            // 这个商品不在购物车，需要新增
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartMapper.insert(cartItem);
        } else {
            // 产品已存在
            cart.setQuantity(cart.getQuantity() + count);
            cart.setChecked(Const.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        // 拼接响应格式
        CartResultVo cartResultVo = getCartResultVoLimit(userId);
        // 返回
        return HandlerResult.success(cartResultVo);
    }

    /**
     * 得到此用户的购物车列表
     *
     * @param userId
     * @return
     */
    private CartResultVo getCartResultVoLimit(Integer userId) {
        // 初始化总价
        BigDecimal cartTotalPrice = new BigDecimal("0");
        // 用户的购物车
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        // 影响给前端的对象外层格式
        CartResultVo cartResultVo = new CartResultVo();
        // Cart -> CartProductVo(前端需要的内层商品对象格式)
        List<CartProductVo> productVoList = Lists.newArrayList();
        // 如果用户的购物车不为空，就拼接内层响应数据格式
        if (HandlerCheck.ObjectIsNotEmpty(cartList)) {
            for (Cart cart : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setUserId(cart.getUserId());
                cartProductVo.setProductId(cart.getProductId());

                // 查询购物车里的对象
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (HandlerCheck.ObjectIsNotEmpty(product)) {
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductSubtitle(product.getSubtitle());

                    // 库存
                    int buyLimitCount = 0;
                    if (cart.getQuantity() >= product.getStock()) {
                        // 库存充足
                        buyLimitCount = cart.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        // 更新购物车中有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cart.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    // 计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.multiply(product.getPrice().doubleValue(),
                            cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cart.getChecked());
                }
                // 判断用户是否选中了此商品
                if (cart.getChecked() == Const.Cart.CHECKED) {
                    // 如果勾选，就增加到整个购物车总价当中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),
                            cartProductVo.getProductTotalPrice().doubleValue());
                }
                productVoList.add(cartProductVo);
            }
        }
        // 拼接外层的响应格式
        cartResultVo.setCartTotalPrice(cartTotalPrice);
        cartResultVo.setCartProductVoList(productVoList);
        cartResultVo.setAllChecked(getAllCheckedStatus(userId));
        cartResultVo.setImageHost(PropertiesUtil.getProperties(Const.Ftp.FTP_SERVER_HTTP_PREFIX_KEY));
        return cartResultVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (HandlerCheck.NumIsEmpty(userId)) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStautsByUserId(userId) == 0;
    }
}
