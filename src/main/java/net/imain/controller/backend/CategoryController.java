package net.imain.controller.backend;

import net.imain.common.Const;
import net.imain.common.HandlerResult;
import net.imain.enums.UserEnum;
import net.imain.pojo.Category;
import net.imain.pojo.User;
import net.imain.service.ICategoryService;
import net.imain.service.IUserService;
import net.imain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

/**
 * @author: uncle
 * @apdateTime: 2017-11-19 10:32
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 获取节点
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<Category> getCategory(@RequestParam(value = "categoryId", defaultValue = "0")
                                                       Integer categoryId) {

        return null;
    }

    /**
     * 新增节点
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> addCategory(@RequestParam(value = "parentId", defaultValue = "0")
                                                     Integer parentId, String categoryName, HttpSession session) {
        // 1.判断用户是否登录
        UserInfoVo userInfoVo = (UserInfoVo) session.getAttribute(Const.CURRENT_USER);
        if (userInfoVo == null) {
            return HandlerResult.error(UserEnum.NEED_LOGIN.getCode(), UserEnum.NEED_LOGIN.getMessage());
        }
        // 2.判断用户是否是管理员
        HandlerResult adminRole = iUserService.checkAdminRole(userInfoVo);
        if (!adminRole.isSuccess()) {
            return HandlerResult.error(UserEnum.IS_NOT_ADMIN.getMessage());
        }
        // 3.处理分类逻辑
        return iCategoryService.addCategory(parentId, categoryName);
    }

    /**
     * 更新分类名称
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> setCategoryName(HttpSession session,
                                         Integer categoryId, String categoryName) {
        // 1.判断用户是否登录
        UserInfoVo userInfoVo = (UserInfoVo) session.getAttribute(Const.CURRENT_USER);
        if (userInfoVo == null) {
            return HandlerResult.error(UserEnum.NEED_LOGIN.getCode(), UserEnum.NEED_LOGIN.getMessage());
        }
        // 2.判断用户是否是管理员
        HandlerResult adminRole = iUserService.checkAdminRole(userInfoVo);
        if (!adminRole.isSuccess()) {
            return HandlerResult.error(UserEnum.IS_NOT_ADMIN.getMessage());
        }
        // 3.更新分类名称
        return iCategoryService.setCategoryName(categoryId, categoryName);
    }
}