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
     * @param id product id
     * @param name product name
     * @return product List
     */
    List<Product> selectByNameAndPrimaryKey(@Param(value = "productId") Integer productId,
                                            @Param(value = "productName") String productName);
}