package net.imain.common;

/**
 * @author: uncle
 * @apdateTime: 2017-11-16 17:07
 */
public enum ResponseEnum {

    SUCCESS(0, "成功"),
    ERROR(1, "错误"),
    NEED_LOGIN(10, "需要登录"),
    ILLEGAL_ARGUMENT(2, "参数错误"),;

    private final Integer code;
    private final String message;

    ResponseEnum(Integer code, String message) {
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
