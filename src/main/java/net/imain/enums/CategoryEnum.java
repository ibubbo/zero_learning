package net.imain.enums;

/**
 * 分类枚举类
 *
 * @author: uncle
 * @apdateTime: 2017-11-19 11:18
 */
public enum  CategoryEnum {
    CATEGORY_EXIST(50, "分类已存在"),

    ADD_CATEGORY_ERROR(51, "添加分类失败"),

    UPDATE_CATEGORY_NAME_ERROR(52, "更新分类名称失败")

    ;

    private final Integer code;
    private final String message;

    CategoryEnum(Integer code, String message) {
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
