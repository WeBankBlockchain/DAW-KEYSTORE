package com.webank.wsdaw.keystore.vo.response;

import lombok.Data;

@Data
public class GetUserKeysResponse {

    private String publicKey;
    private String privateKey;
}
