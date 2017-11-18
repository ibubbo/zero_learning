package net.imain.enums;

/**
 * 用户状态码
 *
 * @author: uncle
 * @apdateTime: 2017-11-16 17:07
 */
public enum UserEnum {

    SUCCESS(0, "成功"),

    ERROR(1, "错误"),

    NEED_LOGIN(10, "用户需要登录"),

    ILLEGAL_ARGUMENT(2, "参数错误"),

    OLD_PASSWORD_ERROR(3, "旧密码错误"),

    USERNAME_NOT_EXIST(4, "用户名不存在"),

    PASSWORD_ERROR(5, "密码错误"),

    USERNAME_EXIST(6, "用户名已存在"),

    EMAIL_EXIST(7, "邮箱已存在"),

    QUESTION_IS_NULL(8, "密保问题为空"),

    GET_BACK_PASSWORD_ERROR(9, "找回密码失败"),

    TOKEN_INEFFECTIVENESS(11, "token无效"),

    UPDATE_PASSWORD_ERROR(12, "更新密码错误"),

    REGISTER_ERROR(13, "注册失败")

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
