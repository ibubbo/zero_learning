package net.imain.enums;

/**
 * 全局枚举类
 *
 * @author: uncle
 * @apdateTime: 2017-11-19 11:23
 */
public enum HandlerEnum {
    SUCCESS(0, "操作成功"),

    ERROR(1, "操作发生了错误"),

    ILLEGAL_ARGUMENT(2, "参数错误"),

    SERVER_EXCEPTION(-1, "服务端异常")
    ;

    private final Integer code;
    private final String message;

    HandlerEnum(Integer code, String message) {
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
