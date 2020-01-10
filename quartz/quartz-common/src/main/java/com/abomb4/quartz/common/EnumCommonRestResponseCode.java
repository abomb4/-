package com.abomb4.quartz.common;

import java.util.*;

/**
 * 通用类型返回码
 *
 * @author abomb4
 */
public enum EnumCommonRestResponseCode {

    // 注意，禁止返回 401 代码，会跳转登录页面

    /** 成功状态 */
    OK(200, "00000", "Success"),
    /** 业务错误 */
    BusinessError(200, "00002", "业务错误"),
    /** 业务错误 */
    AlreadyExists(500, "00003", "已经存在"),
    /** 业务错误 */
    InvalidParameter(400, "10002", "参数校验失败"),
    /** 没有权限 */
    NoAccess(403, "00001", "没有权限"),
    /** 未找到 数据 */
    NotFound(404, "00002", "未找到数据"),
    /** 网络通信异常 */
    NetworkError(500, "00001", "网络通信异常"),
    /** 未知异常 */
    Error(500, "00001", "系统异常"),
    /** 该接口尚未实现 */
    NotImplemented(500, "00009", "该接口尚未实现")
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

    private static final Map<String, EnumCommonRestResponseCode> CODE_MAP;
    static {
        final EnumCommonRestResponseCode[] values = EnumCommonRestResponseCode.values();
        final HashMap<String, EnumCommonRestResponseCode> map = new HashMap<>(values.length);
        for (final EnumCommonRestResponseCode value : values) {
            map.put(value.code, value);
        }
        CODE_MAP = map;
    }

    EnumCommonRestResponseCode(int httpStatus, String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据响应码获取枚举
     *
     * @param code 响应码
     * @return 枚举，若传入 null 或找不到则为 {@link Optional#empty()}
     */
    public static Optional<EnumCommonRestResponseCode> getByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(CODE_MAP.get(code));
    }
}
