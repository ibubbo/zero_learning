package net.imain.service.impl;

import net.imain.common.HandlerResult;
import net.imain.dao.CategoryMapper;
import net.imain.enums.CategoryEnum;
import net.imain.enums.HandlerEnum;
import net.imain.pojo.Category;
import net.imain.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分类管理服务实现层
 *
 * @author: uncle
 * @apdateTime: 2017-11-19 10:51
 */
@Service(value = "iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public HandlerResult<String> addCategory(Integer parentId, String categoryName) {
        // 1.校验父类id和分类名
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 2.判断分类名称是否重复
        Integer resultSum = categoryMapper.checkCategoryName(categoryName);
        if (resultSum > 0) {
            return HandlerResult.error(CategoryEnum.CATEGORY_EXIST.getMessage());
        }
        // 3.补全信息
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        // true - 可用，false - 废弃
        category.setStatus(true);
        // 4.添加分类
        int resultCategorySum = categoryMapper.insert(category);
        if (resultCategorySum == 0) {
            return HandlerResult.error(CategoryEnum.ADD_CATEGORY_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<String> setCategoryName(Integer categoryId, String categoryName) {
        // 1.校验id和分类名
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        Integer resultSum = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultSum == 0) {
            return HandlerResult.error(CategoryEnum.UPDATE_CATEGORY_NAME_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }
}
