package com.webank.wsdaw.keystore.vo.response;

import lombok.Data;

@Data
public class GenerateUserKeysResponse {

    private String weworkUid;
    private String publicKey;
}
