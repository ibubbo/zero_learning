package net.imain.service;

import com.github.pagehelper.PageInfo;
import net.imain.common.HandlerResult;
import net.imain.pojo.Product;
import net.imain.vo.ProductDetailVo;

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

    /**
     * 获取商品详情
     *
     * @param productId
     * @return
     */
    HandlerResult<ProductDetailVo> manageProductDetail(Integer productId);

    /**
     * Product pagination
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    HandlerResult<PageInfo> list(Integer pageNum, Integer pageSize);

    /**
     * Product search
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    HandlerResult<PageInfo> search(String productName, Integer productId, Integer pageNum, Integer pageSize);

    /**
     * portal product search
     *
     * @param productId
     * @return
     */
    HandlerResult<Product> portalProductDetail(Integer productId);

    /**
     * search keyword and categoryId
     *
     * @param categoryId
     * @param keyword
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    HandlerResult<PageInfo> portalList(Integer categoryId, String keyword,
                                 String orderBy, Integer pageNum, Integer pageSize);
}
