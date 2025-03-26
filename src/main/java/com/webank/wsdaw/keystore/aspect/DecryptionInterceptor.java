package com.webank.wsdaw.keystore.aspect;

import cn.hutool.crypto.symmetric.AES;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
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
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {
                MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class,
                CacheKey.class,
                BoundSql.class
            }),
})
public class DecryptionInterceptor implements Interceptor {

    @Autowired private AES aes;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result instanceof ArrayList) {
            @SuppressWarnings("rawtypes")
            ArrayList list = (ArrayList) result;
            if (list.size() == 0) {
                return result;
            }
            if (list.get(0) instanceof Encrypted) {
                for (Object item : list) {
                    decryptField((Encrypted) item);
                }
            }
            return result;
        }
        if (result instanceof Encrypted) {
            decryptField((Encrypted) result);
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}

    private void decryptField(Encrypted object)
            throws IllegalAccessException, NoSuchFieldException {
        String[] encryptFields = object.getEncryptFields();
        Class<?> clazz = object.getClass();

        if (encryptFields.length == 0) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Encrypt encrypt = field.getAnnotation(Encrypt.class);
                if (encrypt != null && field.get(object) != null) {
                    String encryptString = aes.decryptStr((field.get(object).toString()));
                    if (encryptString != null) {
                        field.set(object, encryptString);
                        log.debug("Encryption interceptor，encrypt field: {}", field.getName());
                    }
                }
            }
        } else {
            for (String fieldName : encryptFields) {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                String encryptString = aes.decryptStr((field.get(object).toString()));
                if (encryptString != null && encryptString.length() > 0) {
                    field.set(object, encryptString);
                    log.debug("Encryption interceptor，encrypt field: {}", field.getName());
                }
            }
        }
    }
}
