package net.imain.service;

import net.imain.common.HandlerResult;
import net.imain.pojo.Product;

/**
 * @author: uncle
 * @apdateTime: 2017-11-20 12:41
 */
public interface ProductService {
    /**
     * 更新商品
     *
     * @param product
     * @return
     */
    HandlerResult saveOrUpdate(Product product);

    /**
     * 更新商品状态
     *
     * @param productId 商品id
     * @param status 商品状态
     * @return
     */
    HandlerResult<String> setSaleStatus(Integer productId, Integer status);
}
