package net.imain.enums;

/**
 * @author: uncle
 * @apdateTime: 2017-12-04 17:21
 */
public enum OrderEnum {
    USER_HAS_NO_ORDER(201, "用户没有该订单"),
    ORDER_GENERATION_ERROR(202, "生成订单错误"),

    ;

    private final Integer code;
    private final String message;

    OrderEnum(Integer code, String message) {
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
