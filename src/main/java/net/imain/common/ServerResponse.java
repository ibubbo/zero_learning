package net.imain.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author: uncle
 * @apdateTime: 2017-11-16 16:58
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private Integer status;
    private String msg;
    private T data;

    private ServerResponse(Integer status) {
        this.status = status;
    }

    private ServerResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    // 加此注解，不会被JSON序列化
    @JsonIgnore
    public boolean isSuccess() {
        return this.status.equals(ResponseEnum.SUCCESS.getCode());
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

    public static <T> ServerResponse<T> success() {
        return new ServerResponse<T>(ResponseEnum.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> success(String msg) {
        return new ServerResponse<T>(ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> success(T data) {
        return new ServerResponse<T>(ResponseEnum.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> success(String msg, T data) {
        return new ServerResponse<T>(ResponseEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> error() {
        return new ServerResponse<T>(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
    }

    public static <T> ServerResponse<T> error(String msg) {
        return new ServerResponse<T>(ResponseEnum.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> error(Integer code, String msg) {
        return new ServerResponse<T>(code, msg);
    }
}
