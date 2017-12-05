package net.imain.controller.portal;

import com.github.pagehelper.PageInfo;
import net.imain.common.HandlerConverter;
import net.imain.common.HandlerResult;
import net.imain.pojo.Product;
import net.imain.service.ProductService;
import net.imain.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: uncle
 * @apdateTime: 2017-11-22 12:30
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("detail.do")
    @ResponseBody
    public HandlerResult<ProductDetailVo> detail(Integer productId) {
        Product product = productService.portalProductDetail(productId).getData();
        ProductDetailVo productDetailVo = HandlerConverter.productToProductDetailVo(product);
        return HandlerResult.success(productDetailVo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public HandlerResult<PageInfo> list(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return productService.portalList(categoryId, keyword, orderBy, pageNum, pageSize);
    }
}
