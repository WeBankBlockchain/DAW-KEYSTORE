package com.webank.wsdaw.keystore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(value = "com.webank.wsdaw.keystore.dao.mapper")
public class KeyStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeyStoreApplication.class, args);
    }
}
