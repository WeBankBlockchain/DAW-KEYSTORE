package com.webank.wsdaw.keystore.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.webank.wsdaw.keystore.dao.entity.KeystoreEntity;
import com.webank.wsdaw.keystore.dao.mapper.KeystoreMapper;
import com.webank.wsdaw.keystore.enums.CodeEnum;
import com.webank.wsdaw.keystore.service.KeystoreService;
import com.webank.wsdaw.keystore.vo.request.GenerateUserKeysRequest;
import com.webank.wsdaw.keystore.vo.request.GetUserKeysRequest;
import com.webank.wsdaw.keystore.vo.response.CommonResponse;
import com.webank.wsdaw.keystore.vo.response.GenerateUserKeysResponse;
import com.webank.wsdaw.keystore.vo.response.GetUserKeysResponse;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class KeystoreServiceImpl implements KeystoreService {

    @Autowired private KeystoreMapper keystoreMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<GenerateUserKeysResponse> generateUserKeys(
            GenerateUserKeysRequest request) {
        KeystoreEntity keystoreEntity = new KeystoreEntity();
        keystoreEntity.setWeworkUid(request.getWeworkUserId());
        KeystoreEntity entity = keystoreMapper.getUserKeys(keystoreEntity);
        if (entity != null) {
            GenerateUserKeysResponse resp = new GenerateUserKeysResponse();
            BeanUtil.copyProperties(entity, resp, false);
            return CommonResponse.success(resp);
        }

        String publicKey = "";
        String privateKey = "";
        try {
            // 初始化 Key 生成器，指定算法类型为 RSA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            // 密钥长度为 512 位
            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            publicKey = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
            privateKey = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        } catch (Exception e) {
            log.error("OnError generate key pair failed.", e);
            return CommonResponse.error(CodeEnum.UNKNOWN_ERROR, e.getMessage());
        }

        KeystoreEntity newEntity = new KeystoreEntity();
        newEntity.setWeworkUid(request.getWeworkUserId());
        newEntity.setPublicKey(publicKey);
        newEntity.setPrivateKey(privateKey);
        keystoreMapper.insertUserKeys(newEntity);
        log.info("keystore store succeed {}", JSONUtil.toJsonPrettyStr(newEntity));

        GenerateUserKeysResponse response = new GenerateUserKeysResponse();
        BeanUtil.copyProperties(newEntity, response, false);

        return CommonResponse.success(response);
    }

    @Override
    public CommonResponse<GetUserKeysResponse> getUserKeys(GetUserKeysRequest request) {
        // TODO: 校验用户身份安全控制
        KeystoreEntity entity = new KeystoreEntity();
        entity.setWeworkUid(request.getWeworkUserId());
        KeystoreEntity keystoreEntity = keystoreMapper.getUserKeys(entity);
        if (keystoreEntity == null) {
            log.error("user not existed: {}", request.getWeworkUserId());
            return CommonResponse.error(CodeEnum.USER_KEY_PAIR_NOT_EXIST);
        }

        GetUserKeysResponse response = new GetUserKeysResponse();
        BeanUtil.copyProperties(keystoreEntity, response, false);

        return CommonResponse.success(response);
    }
}
