package net.imain.dao;

import net.imain.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * Query all product
     *
     * @return List
     */
    List<Product> findAll();

    /**
     * Search product
     *
     * @param productId product id
     * @param productName product name
     * @return product List
     */
    List<Product> selectByNameAndPrimaryKey(@Param(value = "productId") Integer productId,
                                            @Param(value = "productName") String productName);

    List<Product> selectByNameAndCategoryIds(@Param(value = "productName") String productName,
                                             @Param(value = "categoryIdList") List<Integer> categoryIdList);

    /**
     * 根据商品ID查询商品信息
     *
     * @param productIdList
     * @return
     */
    List<Product> selectProductByProductIds(@Param(value = "productIdList") List<String> productIdList);

    /**
     * 批量增加商品库存
     *
     * @param productList
     * @return
     */
    int updateProductStockList(@Param(value = "productList") List<Product> productList);
}