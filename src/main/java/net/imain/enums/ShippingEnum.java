package net.imain.enums;

/**
 * @author: uncle
 * @apdateTime: 2017-12-03 10:32
 */
public enum  ShippingEnum {
    SHIPPING_UPDATE_ERROR(150, "地址信息更新失败"),
    SHIPPING_INSERT_ERROR(151, "地址信息添加失败"),
    SHIPPING_DELETE_ERROR(152, "删除地址失败"),
    ;

    private final Integer code;
    private final String message;

    ShippingEnum(Integer code, String message) {
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
