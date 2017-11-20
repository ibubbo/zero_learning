package net.imain.controller.backend;

import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.pojo.Category;
import net.imain.service.CategoryService;
import net.imain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author: uncle
 * @apdateTime: 2017-11-19 10:32
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增节点
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> addCategory(@RequestParam(value = "parentId", defaultValue = "0")
                                                     Integer parentId, String categoryName, HttpSession session) {
        // 校验
        HandlerResult resultCheck = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!resultCheck.isSuccess()) {
            return resultCheck;
        }
        // 处理分类逻辑
        return categoryService.addCategory(parentId, categoryName);
    }

    /**
     * 更新分类名称
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> setCategoryName(HttpSession session,
                                                 Integer categoryId, String categoryName) {
        // 1.数据校验
        HandlerResult resultCheck = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!resultCheck.isSuccess()) {
            return resultCheck;
        }
        // 2.更新分类名称
        return categoryService.setCategoryName(categoryId, categoryName);
    }

    /**
     * 获取品类子节点(平级)
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<List<Category>> getCategory(HttpSession session,
                                                     @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        // 校验
        HandlerResult resultCheck = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!resultCheck.isSuccess()) {
            return resultCheck;
        }
        // 获取当前分类的父类id，遍历所有此id的子节点id
        return categoryService.getCategory(categoryId);
    }

    /**
     * 获取当前id的所有直接或间接的子节点
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<List<Integer>> getCategoryAndDeepChildrenCategory(HttpSession session,
                                                                           @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        // 校验
        HandlerResult resultCheck = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!resultCheck.isSuccess()) {
            return resultCheck;
        }
        // 2.查询
        return categoryService.selectCategoryAndDeepChildrenCategory(categoryId);
    }
}