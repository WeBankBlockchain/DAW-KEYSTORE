package com.webank.wsdaw.keystore.config;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = true)
@Slf4j
public class BeanConfig {

    @Autowired private SystemConfig systemConfig;

    @Bean
    public AES aes() {
        return SecureUtil.aes(systemConfig.getEncryptPrivateKey().getBytes());
    }
}
