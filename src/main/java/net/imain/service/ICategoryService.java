package net.imain.service;

import net.imain.common.HandlerResult;

/**
 * 分类管理接口层
 *
 * @author: uncle
 * @apdateTime: 2017-11-19 10:51
 */
public interface ICategoryService {

    /**
     * 添加分类
     *
     * @param parentId 分类id
     * @param categoryName 分类名称
     * @return
     */
    HandlerResult<String> addCategory(Integer parentId, String categoryName);

    /**
     * 根据分类ID更新分类名称
     *
     * @param categoryId 分类id
     * @param categoryName 分类需要更新的名称
     * @return
     */
    HandlerResult<String> setCategoryName(Integer categoryId, String categoryName);
}
