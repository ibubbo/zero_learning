package net.imain.common;

import net.imain.enums.HandlerEnum;
import net.imain.enums.UserEnum;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author: uncle
 * @apdateTime: 2017-11-16 16:58
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HandlerResult<T> implements Serializable {
    private Integer status;
    private String msg;
    private T data;

    private HandlerResult(Integer status) {
        this.status = status;
    }

    private HandlerResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private HandlerResult(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    private HandlerResult(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    // 加此注解，不会被JSON序列化
    @JsonIgnore
    public boolean isSuccess() {
        return this.status.equals(HandlerEnum.SUCCESS.getCode());
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> HandlerResult<T> success() {
        return new HandlerResult<T>(HandlerEnum.SUCCESS.getCode());
    }

    public static <T> HandlerResult<T> success(String msg) {
        return new HandlerResult<T>(HandlerEnum.SUCCESS.getCode(), msg);
    }

    public static <T> HandlerResult<T> success(T data) {
        return new HandlerResult<T>(HandlerEnum.SUCCESS.getCode(), data);
    }

    public static <T> HandlerResult<T> success(String msg, T data) {
        return new HandlerResult<T>(HandlerEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> HandlerResult<T> error() {
        return new HandlerResult<T>(HandlerEnum.ERROR.getCode(), HandlerEnum.ERROR.getMessage());
    }

    public static <T> HandlerResult<T> error(String msg) {
        return new HandlerResult<T>(HandlerEnum.ERROR.getCode(), msg);
    }

    public static <T> HandlerResult<T> error(Integer code, String msg) {
        return new HandlerResult<T>(code, msg);
    }
}
