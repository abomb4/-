package com.abomb4.quartz.common;

import lombok.Data;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Standard Response
 *
 * @author abomb4
 */
@Data
public class StandardResponse<T> implements Serializable {

    /**
     * 返回码，可以参阅 {@link EnumCommonRestResponseCode}
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    public StandardResponse() {
    }

    public StandardResponse(final String code, final String msg,
                        final T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public StandardResponse(EnumCommonRestResponseCode code, final T data, final String msg) {
        this.code = code.getCode();
        this.msg = (msg == null || msg.length() == 0) ? code.getMessage() : msg;
        this.data = data;
    }

    /**
     * 判断响应值是否是 00000 成功状态
     *
     * @return code 是否为 00000
     */
    public boolean isSuccess() {
        return EnumCommonRestResponseCode.OK.getCode().equals(code);
    }

    /**
     * 将 RestResponse 转换成另一种返回格式
     *
     * @param mapper data 转换器
     * @param <R>    转换后的类型
     * @return 转换结果，如果是失败的话，还是失败；如果是成功则进行 mapper 转换
     */
    public <R> StandardResponse<R> map(Function<T, R> mapper) {
        if (this.data != null) {
            final R newData = mapper.apply(this.data);
            return new StandardResponse<>(this.code, this.msg, newData);
        } else {
            return new StandardResponse<>(this.code, this.msg, null);
        }
    }

    /**
     * 创建一个成功响应
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 响应类
     */
    public static <T> StandardResponse<T> success(T data) {
        return success(data, null);
    }

    /**
     * 创建一个成功响应
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 响应类
     */
    public static <T> StandardResponse<T> success(T data, String message) {
        return new StandardResponse<>(EnumCommonRestResponseCode.OK, data, message);
    }

    /**
     * 创建一个失败响应
     *
     * @param code    通用错误码
     * @param message 错误信息
     * @param <T>     数据类型
     * @return 响应类
     */
    public static <T> StandardResponse<T> fail(EnumCommonRestResponseCode code, String message) {
        if (EnumCommonRestResponseCode.OK.equals(code)) {
            throw new IllegalArgumentException("不能在失败请求的创建方法中传入成功代码");
        }
        return new StandardResponse<>(code, null, message);
    }

    /**
     * 创建一个失败响应
     *
     * @param code 通用错误码
     * @param <T>  数据类型
     * @return 响应类
     */
    public static <T> StandardResponse<T> fail(EnumCommonRestResponseCode code) {
        if (EnumCommonRestResponseCode.OK.equals(code)) {
            throw new IllegalArgumentException("不能在失败请求的创建方法中传入成功代码");
        }
        return new StandardResponse<>(code, null, code.getMessage());
    }

    /**
     * 创建一个失败响应
     *
     * @param <T>     数据类型
     * @param code    通用或自定义错误码
     * @param message 错误信息
     * @return 响应类
     */
    public static <T> StandardResponse<T> fail(String code, String message) {
        if (EnumCommonRestResponseCode.OK.getCode().equals(code)) {
            throw new IllegalArgumentException("不能在失败请求的创建方法中传入成功代码");
        }
        return new StandardResponse<>(code, message, null);
    }
}
