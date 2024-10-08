package com.webank.wsdaw.keystore.vo.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserKeysRequest extends CommonRequest {

    @NotBlank(message = "企微用户ID不能为空.")
    private String weworkUserId;
}
