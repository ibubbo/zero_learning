package net.imain.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerConverter;
import net.imain.common.HandlerResult;
import net.imain.dao.CategoryMapper;
import net.imain.dao.ProductMapper;
import net.imain.enums.CategoryEnum;
import net.imain.enums.HandlerEnum;
import net.imain.enums.ProductEnum;
import net.imain.pojo.Category;
import net.imain.pojo.Product;
import net.imain.service.ProductService;
import net.imain.vo.ProductDetailVo;
import net.imain.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (HandlerCheck.ObjectIsEmpty(category)) {
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
        // 判断商品状态是否为空，不为空默认 = 1 上架
        Integer orStatus = Optional.of(product)
                .map(pro -> pro.getStatus())
                .orElse(1);
        // 添加商品信息
        product.setStatus(orStatus);
        int insertProduct = productMapper.insert(product);
        if (insertProduct == 0) {
            return HandlerResult.error(ProductEnum.SAVE_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<String> setSaleStatus(Integer productId, Integer status) {
        // 数据校验
        if (HandlerCheck.NumIsEmpty(productId) || HandlerCheck.NumIsEmpty(status)) {
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

    @Override
    public HandlerResult<ProductDetailVo> manageProductDatail(Integer productId) {
        // 数据校验
        if (HandlerCheck.NumIsEmpty(productId)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 查询商品信息
        Product product = productMapper.selectByPrimaryKey(productId);
        if (HandlerCheck.ObjectIsEmpty(product)) {
            return HandlerResult.error(ProductEnum.PRODUCT_NOT_EXIST.getMessage());
        }
        // 补全商品信息
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        ProductDetailVo productDetailVo = HandlerConverter.productToProductVo(product);
        productDetailVo.setParentCategoryId(
                Optional.ofNullable(category).isPresent() ? category.getParentId() : 0
        );
        // 返回
        return HandlerResult.success(productDetailVo);
    }

    @Override
    public HandlerResult<PageInfo> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductListVo> productListVos = productMapper.findAll().stream()
                .map(product ->
                        HandlerConverter.productToProductListVo(product)
                ).collect(Collectors.toList());
        PageInfo<ProductListVo> pageInfo = new PageInfo<>(productListVos);
        return HandlerResult.success(pageInfo);
    }

    @Override
    public HandlerResult<PageInfo> search(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<ProductListVo> listVos = productMapper.selectByNameAndPrimaryKey(productId, productName).stream()
                .map(product -> {
                    ProductListVo productListVo = HandlerConverter.productToProductListVo(product);
                    // 转换完之后将商品状态置空
                    productListVo.setStatus(null);
                    return productListVo;
                })
                .collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(listVos);
        return HandlerResult.success(pageInfo);
    }
}
