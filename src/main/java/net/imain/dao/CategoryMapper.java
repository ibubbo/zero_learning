package net.imain.dao;

import net.imain.common.HandlerResult;
import net.imain.pojo.Category;

import java.util.List;

/**
 * 分类接口层
 *
 * @author uncle
 */
public interface CategoryMapper {

    /**
     * 根据主键删除分类
     *
     * @param id 主键
     * @return 删除成功的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增分类
     *
     * @param record 分类对象
     * @return 新增成功的数量
     */
    int insert(Category record);

    /**
     * 新增分类（字段判空）
     *
     * @param record 分类对象
     * @return 新增成功的数量
     */
    int insertSelective(Category record);

    /**
     * 根据主键查询分类
     *
     * @param id 主键
     * @return 添加成功的分类
     */
    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    /**
     * 校验分类名称是否重复
     *
     * @param categoryName
     * @return
     */
    Integer checkCategoryName(String categoryName);

    /**
     * 获得所有子节点的id
     *
     * @param parentId
     * @return
     */
    List<Category> selectCategoryChildrenByParentId(Integer parentId);
}