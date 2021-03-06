package net.imain.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 常量类
 *
 * @author: uncle
 * @apdateTime: 2017-11-16 18:56
 */
public class Constants {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String TOKEN_PREFIX = "token_";

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_VALUE = "http://img.imooc.com/";

    /**
     * 角色接口：
     * 0: 管理员
     * 1: 普通用户
     */
    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface Ftp {
        String FTP_SERVER_HTTP_PREFIX_KEY = "ftp.server.http.prefix";
        Integer PORT = 21;
        String FTP_IP_KEY = "ftp.server.ip";
        String FTP_USER_KEY = "ftp.user";
        String FTP_PASS = "ftp.pass";
        String FTP_FILEPATH = "ftp.filePath";
    }

    public enum ProductStatusEnum {
        SALE(1, "在售"),
        DISCONTINUED_SALE(2, "停售"),
        REMOVE(3, "删除");
        private int status;
        private String value;

        public int getStatus() {
            return status;
        }

        public String getValue() {
            return value;
        }

        ProductStatusEnum(int status, String value) {
            this.status = status;
            this.value = value;
        }
    }

    public interface ProductListOrderBy {
        // list -> O(n) , set -> O(1)
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Cart {
        int CHECKED = 1;
        int UNCHECKED = 2;
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    /**
     * 订单状态
     */
    public enum OrderStatusEnum {
        CANSELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已支付"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");

        private int code;
        private String value;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

    /**
     * 交易状态
     */
    public interface AlipayCallback {

        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY"; // 交易创建，等待买家付款
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS"; // 交易支付成功

        String RESPONSE_SUCCESS = "success";// 返回成功
        String RESPONSE_FAILED = "failed"; // 返回失败
    }

    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        private int code;
        private String value;

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentTypeEnum {
        ONELINE_PAY(1, "在线支付");
        private int code;
        private String value;

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

}
