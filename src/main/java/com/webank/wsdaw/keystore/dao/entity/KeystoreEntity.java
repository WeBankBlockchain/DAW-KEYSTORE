package com.webank.wsdaw.keystore.dao.entity;

import com.webank.wsdaw.keystore.aspect.Encrypt;
import com.webank.wsdaw.keystore.aspect.Encrypted;
import lombok.Data;

@Data
public class KeystoreEntity implements Encrypted {

    private Long pkId;
    @Encrypt private String weworkUid;
    private String publicKey;
    private String privateKey;
}
