package net.imain.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 常量类
 *
 * @author: uncle
 * @apdateTime: 2017-11-16 18:56
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String TOKEN_PREFIX = "token_";

    public static final String TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static final String DEFAULT_VALUE = "http://img.imooc.com/";

    /**
     * 角色接口：
     *  0: 管理员
     *  1: 普通用户
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
        REMOVE(3, "删除")
        ;
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
}
