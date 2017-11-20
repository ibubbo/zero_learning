package net.imain.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.imain.common.HandlerResult;
import net.imain.dao.CategoryMapper;
import net.imain.enums.CategoryEnum;
import net.imain.enums.HandlerEnum;
import net.imain.pojo.Category;
import net.imain.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 分类管理服务实现层
 *
 * @author: uncle
 * @apdateTime: 2017-11-19 10:51
 */
@Service(value = "categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

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
        // 2.补全信息
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        Integer resultSum = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultSum == 0) {
            return HandlerResult.error(CategoryEnum.UPDATE_CATEGORY_NAME_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<List<Category>> getCategory(Integer categoryId) {
        // 根据categoryId拿到它的子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.error("未找到子分类，categoryList: {}", categoryList);
            return HandlerResult.error(CategoryEnum.CHILD_NOT_EXIST.getMessage());
        }
        return HandlerResult.success(categoryList);
    }

    @Override
    public HandlerResult<List<Integer>> selectCategoryAndDeepChildrenCategory(Integer categoryId) {
        // 存放放回的id容器
        Set<Category> categorySet = Sets.newHashSet();
        // 用来返回的对象
        List<Integer> categoryIdList = Lists.newArrayList();
        // 递归
        findChildCategory(categorySet, categoryId)
                .forEach(categoryItem -> categoryIdList.add(categoryItem.getId()));

        return HandlerResult.success(categoryIdList);
    }

    /**
     * 递归函数，考虑排重
     */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        // 添加当前id
        Optional.ofNullable(categoryMapper.selectByPrimaryKey(categoryId))
                .ifPresent(category -> categorySet.add(category));
        // 根据当前id查找子节点
        categoryMapper.selectCategoryChildrenByParentId(categoryId)
                .forEach(categoryItem -> findChildCategory(categorySet, categoryItem.getId()));
        return categorySet;
    }
}
