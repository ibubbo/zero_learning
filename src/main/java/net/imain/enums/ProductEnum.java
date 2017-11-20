package net.imain.enums;

/**
 * 商品
 *
 * @author: uncle
 * @apdateTime: 2017-11-20 13:29
 */
public enum ProductEnum {
    SAVE_ERROR(100, "添加失败"),

    UPDATE_ERROR(101, "更新失败")
    ;

    private final Integer code;

    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ProductEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
