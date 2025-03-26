package com.webank.wsdaw.keystore.aspect;

import cn.hutool.crypto.symmetric.AES;
import java.lang.reflect.Field;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Intercepts({
    @Signature(
            method = "update",
            type = Executor.class,
            args = {MappedStatement.class, Object.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
public class EncryptionInterceptor implements Interceptor {

    @Autowired private AES aes;

    public EncryptionInterceptor() {}

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;

        for (Object object : args) {
            // 从MappedStatement参数中获取到操作类型
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                sqlCommandType = ms.getSqlCommandType();
                log.debug("Encryption interceptor type： {}", sqlCommandType);
                continue;
            }
            log.debug("Encryption interceptor param：{}", object);

            // 判断参数
            if (object instanceof Encrypted) {
                if (SqlCommandType.INSERT == sqlCommandType) {
                    encryptField((Encrypted) object);
                    continue;
                }
                if (SqlCommandType.UPDATE == sqlCommandType) {
                    encryptField((Encrypted) object);
                    log.debug("Encryption interceptor update operation,encrypt field: {}", object);
                }
                if (SqlCommandType.SELECT == sqlCommandType) {
                    encryptField((Encrypted) object);
                    log.debug("Encryption interceptor select operation,encrypt field: {}", object);
                }
            }
        }
        return invocation.proceed();
    }

    private void encryptField(Encrypted object)
            throws IllegalAccessException, NoSuchFieldException {
        String[] encryptFields = object.getEncryptFields();
        Class<?> clazz = object.getClass();

        if (encryptFields.length == 0) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Encrypt encrypt = field.getAnnotation(Encrypt.class);
                if (encrypt != null && field.get(object) != null) {
                    String encryptString = aes.encryptBase64((field.get(object).toString()));
                    field.set(object, encryptString);
                    log.debug("Encryption interceptor，encrypt field: {}", field.getName());
                }
            }
        } else {
            for (String fieldName : encryptFields) {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                String encryptString = aes.encryptBase64((field.get(object).toString()));
                field.set(object, encryptString);
                log.debug("Encryption interceptor，encrypt field: {}", field.getName());
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}
}
