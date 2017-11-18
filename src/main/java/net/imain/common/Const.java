package net.imain.common;

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
    /**
     * 角色接口：
     *  0: 管理员
     *  1: 普通用户
     */
    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }
}
