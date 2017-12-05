package net.imain.common;

import net.imain.pojo.Product;
import net.imain.pojo.User;
import net.imain.util.PropertiesUtil;
import net.imain.vo.ProductDetailVo;
import net.imain.vo.ProductListVo;
import net.imain.vo.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import java.util.Date;

/**
 * 全局转换器
 *
 * @author: uncle
 * @apdateTime: 2017-11-18 14:51
 */
public class HandlerConverter {

    /**
     * userInfoVo 转 user
     *
     * @param userInfoVo 数据
     * @return
     */
    public static User userInfoToUser(UserInfoVo userInfoVo) {
        User user = new User();
        BeanUtils.copyProperties(userInfoVo, user);
        return user;
    }

    /**
     * Product are converted into productVo
     *
     * @param product Product information
     * @return ProductVo
     */
    public static ProductDetailVo productToProductDetailVo(Product product) {
        ProductDetailVo vo = new ProductDetailVo();
        BeanUtils.copyProperties(product, vo);
        vo.setImageHost(PropertiesUtil.getProperties(Constants.Ftp.FTP_SERVER_HTTP_PREFIX_KEY, Constants.DEFAULT_VALUE));
        vo.setCreateTime(dateToString(product.getCreateTime()));
        vo.setUpdateTime(dateToString(product.getUpdateTime()));
        return vo;
    }

    /**
     * Product are converted into productListVo
     *
     * @param product Product information
     * @return ProductListVo
     */
    public static ProductListVo productToProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        BeanUtils.copyProperties(product, productListVo);
        // TODO. Whether this is really need
        productListVo.setImageHost(PropertiesUtil.getProperties(Constants.Ftp.FTP_SERVER_HTTP_PREFIX_KEY, Constants.DEFAULT_VALUE));
        return productListVo;
    }

    /**
     * Date converted into string
     *
     * @param date {data}
     * @return {string}
     */
    public static String dateToString(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return new DateTime(date).toString(formatStr);
    }

    public static String dateToString(Date date) {
        return dateToString(date, Constants.TIME_FORMAT);
    }

    /**
     * String converted into date
     *
     * @param dateTimeStr date format
     * @param formatStr converted string
     * @return date
     */
    public static Date strToDate(String dateTimeStr, String formatStr) {
        return DateTimeFormat
                .forPattern(formatStr)
                .parseDateTime(dateTimeStr)
                .toDate();
    }

    public static Date strToDate(String dateTimeStr) {
        return strToDate(dateTimeStr, Constants.TIME_FORMAT);
    }
}
