package net.imain.enums;

/**
 * 商品
 *
 * @author: uncle
 * @apdateTime: 2017-11-20 13:29
 */
public enum ProductEnum {
    PRODUCT_SAVE_ERROR(100, "商品添加失败"),

    PRODUCT_UPDATE_ERROR(101, "商品更新失败"),

    PRODUCT_NOT_EXIST(102, "商品不存在"),

    PRODUCT_HAS_REMOVED(103, "商品已经下架")
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
