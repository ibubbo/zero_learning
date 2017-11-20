package net.imain.service.impl;

import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.dao.CategoryMapper;
import net.imain.dao.ProductMapper;
import net.imain.enums.CategoryEnum;
import net.imain.enums.HandlerEnum;
import net.imain.enums.ProductEnum;
import net.imain.pojo.Product;
import net.imain.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author: uncle
 * @apdateTime: 2017-11-20 12:41
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 新增和保存
     */
    @Override
    public HandlerResult<String> saveOrUpdate(Product product) {
        // 数据校验(商品名称可以重复)
        if (HandlerCheck.ObjectIsEmpty(product)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 校验分类ID
        Integer resultSum = categoryMapper.checkCategoryByPrimaryKey(product.getCategoryId());
        if (resultSum == 0) {
            return HandlerResult.error(CategoryEnum.CATEGORY_ID_NOT_EXIST.getMessage());
        }
        // 设置主图
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] strings = product.getSubImages().split(",");
            if (strings.length > 0) {
                product.setMainImage(strings[0]);
            }
        }
        // 更新商品信息
        if (product.getId() != null) {
            int updateProduct = productMapper.updateByPrimaryKeySelective(product);
            if (updateProduct == 0) {
                return HandlerResult.error(ProductEnum.UPDATE_ERROR.getMessage());
            }
            return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
        }
        // 添加商品信息
        boolean isEmpty = HandlerCheck.NumIsEmpty(product.getStatus());
        product.setStatus(isEmpty ? 1 : product.getStatus());
        int insertProduct = productMapper.insert(product);
        if (insertProduct == 0) {
            return HandlerResult.error(ProductEnum.SAVE_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<String> setSaleStatus(Integer productId, Integer status) {
        // 数据校验
        boolean b = HandlerCheck.NumIsEmpty(productId);
        boolean b1 = HandlerCheck.NumIsEmpty(status);

        if (HandlerCheck.NumIsEmpty(productId) && HandlerCheck.NumIsEmpty(status)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 补全信息
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        // 更新信息
        int updateSum = productMapper.updateByPrimaryKeySelective(product);
        if (updateSum == 0) {
            return HandlerResult.error(ProductEnum.UPDATE_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    public HandlerResult<Product> getDetail(Integer productId) {
        // 数据校验
        if (HandlerCheck.NumIsEmpty(productId)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        return null;
    }
}
