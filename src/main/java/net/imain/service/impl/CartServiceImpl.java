package net.imain.service.impl;

import com.google.common.base.Splitter;
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
import net.imain.service.CartService;
import net.imain.util.BigDecimalUtil;
import net.imain.util.PropertiesUtil;
import net.imain.vo.CartProductVo;
import net.imain.vo.CartResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
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
    public HandlerResult saveOrUpdate(Integer userId, Integer productId, Integer count) {
        // 校验数据
        if (HandlerCheck.NumIsEmpty(count) || HandlerCheck.NumIsEmpty(productId)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (HandlerCheck.ObjectIsEmpty(product)) {
            return HandlerResult.success(ProductEnum.PRODUCT_NOT_EXIST.getMessage());
        }
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        Integer stock = product.getStock();
        if (HandlerCheck.ObjectIsNotEmpty(cart)) {
            // 更新商品信息，判断商品库存，增加库存
            int cartKu = cart.getQuantity() + count;
            if (stock >= cartKu) {
                cart.setQuantity(cartKu);
                // 库存充足
                product.setStock(stock - count);
            } else {
                // 库存不足，在购物车原有的库存数上 + 剩余库存
                cart.setQuantity(cart.getQuantity() + stock);
                product.setStock(-1);
            }
            cartMapper.updateByPrimaryKeySelective(cart);
        } else {
            // 新增商品信息
            cart = new Cart();
            if (stock > count) {
                cart.setQuantity(count);
            } else {
                cart.setQuantity(stock);
                product.setStock(-1);
            }
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setChecked(Const.Cart.CHECKED);
            cartMapper.insert(cart);
            int sum = stock - cart.getQuantity();
            product.setStock(sum < 0 ? -1 : sum);
        }
        // 更新商品
        productMapper.updateByPrimaryKeySelective(product);
        // 转格式
        CartResultVo change = getCartResultVoLimit(userId);
        return HandlerResult.success(change);
    }

    @Override
    public HandlerResult list(Integer userId) {
        CartResultVo resultVoLimit = getCartResultVoLimit(userId);
        return HandlerResult.success(resultVoLimit);
    }

    @Override
    public HandlerResult update(Integer userId, Integer productId, Integer count) {

        int oldCartKu, productStock;

        // 数据检验
        if (HandlerCheck.NumIsEmpty(productId) || HandlerCheck.NumIsEmpty(count)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (HandlerCheck.ObjectIsEmpty(product)) {
            return HandlerResult.success(ProductEnum.PRODUCT_NOT_EXIST.getMessage());
        }
        // 查询购物车
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        productStock = product.getStock();
        if (HandlerCheck.ObjectIsNotEmpty(cart)) {
            oldCartKu = cart.getQuantity();

            // 更新准备
            int value = count - oldCartKu;
            if (productStock - value < 0) {
                product.setStock(-1);
                cart.setQuantity(productStock + oldCartKu);
            } else {
                product.setStock(productStock - value);
                cart.setQuantity(count);
            }
            // 数据更新
            productMapper.updateByPrimaryKeySelective(product);
            int updateByPrimaryKeySelective = cartMapper.updateByPrimaryKeySelective(cart);
            if (updateByPrimaryKeySelective == 0) {
                return HandlerResult.error(ProductEnum.UPDATE_ERROR.getMessage());
            }
        }
        // 格式转换
        CartResultVo cartResultVoLimit = getCartResultVoLimit(userId);
        return HandlerResult.success(cartResultVoLimit);
    }

    @Override
    public HandlerResult delete(Integer userId, String productIds) {

        if (HandlerCheck.ObjectIsEmpty(productIds)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        List<Cart> cartList = cartMapper.selectCartByUserIdAndProductIds(productIdList, userId);
        List<Product> productList = productMapper.selectProductByProductIds(productIdList);
        for (Cart cartItem : cartList) {
            for (Product productItem : productList) {
                if (cartItem.getProductId().equals(productItem.getId())) {
                    productItem.setStock(productItem.getStock() + cartItem.getQuantity());
                }
            }
        }

        productMapper.updateProductStockList(productList);
        // 分别添加给商品库存添加对应的数量
        cartMapper.deleteCartByProductIds(productIdList, userId);
        CartResultVo cartResultVoLimit = getCartResultVoLimit(userId);
        return HandlerResult.success(cartResultVoLimit);
    }

    @Override
    public HandlerResult<CartResultVo> selectAllOrUnSelectAll(Integer userId, boolean isSelectAll) {
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        if (HandlerCheck.ObjectIsNotEmpty(cartList)) {
            if (isSelectAll) {
                // 全选
                for (Cart cartItem : cartList) {
                    if (cartItem.getChecked() == Const.Cart.UNCHECKED) {
                        cartItem.setChecked(Const.Cart.CHECKED);
                    }
                }
            } else {
                // 全不选
                for (Cart cartItem : cartList) {
                    if (cartItem.getChecked() == Const.Cart.CHECKED) {
                        cartItem.setChecked(Const.Cart.UNCHECKED);
                    }
                }
            }
        }
        // 批量修改
        cartMapper.updateCartProductCheckedIsSelectAll(cartList);
        CartResultVo cartResultVoLimit = getCartResultVoLimit(userId);
        return HandlerResult.success(cartResultVoLimit);
    }

    @Override
    public HandlerResult<CartResultVo> selectOrUnSelect(Integer userId, Integer productId, boolean isSelect) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (HandlerCheck.ObjectIsNotEmpty(cart)) {
            // 选
            if (isSelect) {
                cart.setChecked(Const.Cart.CHECKED);
            } else {
            // 不选
                cart.setChecked(Const.Cart.UNCHECKED);
            }
        }
        // 修改
        cartMapper.updateCartProductCheckedIsSelect(productId, cart.getChecked(), userId);
        CartResultVo cartResultVoLimit = getCartResultVoLimit(userId);
        return HandlerResult.success(cartResultVoLimit);
    }

    @Override
    public HandlerResult<Integer> getCartProductCount(Integer userId) {
        // TODO 是否需要考虑选中状态
        return HandlerResult.success(cartMapper.selectCartProductCount(userId));
    }

    /**
     * 校验此购物车中的商品是否是全选状态
     *
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if (HandlerCheck.NumIsEmpty(userId)) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

    /**
     * 拼接前端响应格式
     *
     * @param userId
     * @return
     */
    private CartResultVo getCartResultVoLimit(Integer userId) {
        // 外层数据格式
        CartResultVo cartResultVo = new CartResultVo();
        // 内层数据格式
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        // 商品总价
        BigDecimal productTotalPrice = new BigDecimal("0");
        // 购物车
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        if (HandlerCheck.ObjectIsNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                // 拼接内层格式
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setQuantity(cartItem.getQuantity());
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (HandlerCheck.ObjectIsNotEmpty(product)) {
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setQuantity(cartItem.getQuantity());
                    cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    // 如果是 -1，表示商品库存已不足
                    if (product.getStock() == -1) {
                        cartProductVo.setProductStock(0);
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        product.setStock(0);
                        productMapper.updateByPrimaryKeySelective(product);
                    }
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.multiply(product.getPrice().doubleValue(),
                            cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                // 计算商品总价
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    productTotalPrice = BigDecimalUtil.add(productTotalPrice.doubleValue(),
                            cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
            // 拼接外层格式
            cartResultVo.setImageHost(PropertiesUtil.getProperties(Const.Ftp.FTP_SERVER_HTTP_PREFIX_KEY));
            cartResultVo.setCartProductVoList(cartProductVoList);
            cartResultVo.setAllChecked(getAllCheckedStatus(userId));
            cartResultVo.setCartTotalPrice(productTotalPrice);
        }
        return cartResultVo;
    }

}
