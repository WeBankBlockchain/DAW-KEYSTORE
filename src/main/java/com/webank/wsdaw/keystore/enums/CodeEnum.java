package com.webank.wsdaw.keystore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CodeEnum {
    // 0-request success
    TRANSACTION_SUCCESS(0, "操作成功"),

    // 1000-1999: params validate error
    REQUEST_PARAMS_ERROR(1001, "请求参数错误"),

    // 6000-6999: db error

    // 7000-7999: third service error
    USER_KEY_PAIR_NOT_EXIST(7000, "用户密钥对不存在"),

    // other error

    // 9000-9998: system error
    // 9999-unknown error
    UNKNOWN_ERROR(9999, "系统错误");

    private final int code;
    private final String msg;

    public static CodeEnum getCodeEnum(int code) {
        for (CodeEnum type : CodeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return UNKNOWN_ERROR;
    }
}
