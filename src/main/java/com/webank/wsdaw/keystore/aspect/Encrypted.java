package com.webank.wsdaw.keystore.aspect;

public interface Encrypted {

    default String[] getEncryptFields() {
        return new String[0];
    }
}
