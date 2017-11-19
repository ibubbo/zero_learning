package net.imain.enums;

/**
 * 用户状态码
 *
 * @author: uncle
 * @apdateTime: 2017-11-16 17:07
 */
public enum UserEnum {

    NEED_LOGIN(10, "用户需要登录"),

    OLD_PASSWORD_ERROR(11, "旧密码错误"),

    USERNAME_NOT_EXIST(12, "用户名不存在"),

    PASSWORD_ERROR(13, "密码错误"),

    USERNAME_EXIST(14, "用户名已存在"),

    EMAIL_EXIST(15, "邮箱已存在"),

    QUESTION_IS_NULL(16, "密保问题为空"),

    GET_BACK_PASSWORD_ERROR(17, "找回密码失败"),

    TOKEN_INEFFECTIVENESS(18, "token无效"),

    UPDATE_PASSWORD_ERROR(19, "更新密码错误"),

    REGISTER_ERROR(20, "注册失败"),

    UPDATE_USERINFO_ERROR(21, "更新个人信息失败"),

    NOT_FIND_USERINFO(22, "没有找到用户信息"),

    IS_NOT_ADMIN(23, "用户权限不足")

    ;

    private final Integer code;
    private final String message;

    UserEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
