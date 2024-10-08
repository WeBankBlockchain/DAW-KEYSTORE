package com.webank.wsdaw.keystore.vo.response;

import com.webank.wsdaw.keystore.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommonResponse<T> {

    private int code = 0;
    private String msg = "success";
    private String debugMsg;
    private T data;

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>();
    }

    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setData(data);
        return response;
    }

    public static <T> CommonResponse<T> error(int Code, String Msg) {
        return new CommonResponse<>(Code, Msg, null, null);
    }

    public static <T> CommonResponse<T> error(CodeEnum CodeEnum) {
        return new CommonResponse<>(CodeEnum.getCode(), CodeEnum.getMsg(), null, null);
    }

    public static <T> CommonResponse<T> error(CodeEnum CodeEnum, String debugMsg) {
        return new CommonResponse<>(CodeEnum.getCode(), CodeEnum.getMsg(), debugMsg, null);
    }

    public boolean isSuccess() {
        return code == 0;
    }
}
