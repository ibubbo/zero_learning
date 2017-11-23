package net.imain.controller.backend;

import com.google.common.collect.Maps;
import net.imain.common.Const;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.pojo.Product;
import net.imain.service.FileService;
import net.imain.service.ProductService;
import net.imain.service.UserService;
import net.imain.util.PropertiesUtil;
import net.imain.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Product management
 *
 * @author: uncle
 * @apdateTime: 2017-11-20 11:50
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    @Autowired
    private FileService fileService;
    /**
     * update product
     */
    @RequestMapping(value = "save.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> save(HttpSession session, Product product) {
        // check user is null, and check role
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        return productService.saveOrUpdate(product);
    }

    /**
     * Set product state
     */
    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<String> setSaleStatus(HttpSession session, Integer productId, Integer status) {
        // check user is null, and check role
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // The business logic
        return productService.setSaleStatus(productId, status);
    }

    /**
     * Product information
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult<ProductDetailVo> getDetail(HttpSession session, Integer productId) {
        // check user is null, and check role
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // The business logic
        return productService.manageProductDetail(productId);
    }

    /**
     * Product pagination
     */
    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult list(HttpSession session,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        // check user is null, and check role
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        return productService.list(pageNum, pageSize);
    }

    /**
     * product search
     */
    @RequestMapping(value = "search.do", method = RequestMethod.GET)
    @ResponseBody
    public HandlerResult search(HttpSession session, String productName, Integer productId,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        // Check user is null, and check role
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // The business logic
        return productService.search(productName, productId, pageNum, pageSize);
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public HandlerResult upload(@RequestParam(value = "upload_file", required = false) MultipartFile file,
                                HttpServletRequest request, HttpSession session) {
        // check user is null and check role
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            return handlerResult;
        }
        // path, rename folder(folder name is "upload")
        String localPath = request.getSession().getServletContext().getRealPath("upload");
        String imgPath = new DateTime().toString("/yyyy/MM/dd");
        String fileName = fileService.upload(file, imgPath, localPath);
        String url = PropertiesUtil.getProperties(Const.Ftp.FTP_SERVER_HTTP_PREFIX_KEY) + imgPath + "/" + fileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", fileName);
        fileMap.put("url", url);
        return HandlerResult.success(fileMap);
    }

    @RequestMapping(value = "richtext_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        Map map = Maps.newHashMap();
        // check user is null and check role
        HandlerResult handlerResult = HandlerCheck.checkUserIsPresentAndRole(session, userService);
        if (!handlerResult.isSuccess()) {
            map.put("success", false);
            map.put("msg", handlerResult.getMsg());
            return map;
        }
        String localPath = request.getSession().getServletContext().getRealPath("upload");
        String imgPath = new DateTime().toString("/yyyy/MM/dd");
        String fileName = fileService.upload(file, imgPath, localPath);
        if (StringUtils.isBlank(fileName)) {
            map.put("success", false);
            map.put("msg", HandlerResult.error().getMsg());
            return map;
        }
        String url = PropertiesUtil.getProperties(Const.Ftp.FTP_SERVER_HTTP_PREFIX_KEY) + imgPath + "/" + fileName;

        map.put("success", true);
        map.put("msg", HandlerResult.success().getMsg());
        map.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");

        return map;
    }

}
