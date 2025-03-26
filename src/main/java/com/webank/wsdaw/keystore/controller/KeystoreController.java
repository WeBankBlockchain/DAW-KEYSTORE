package com.webank.wsdaw.keystore.controller;

import com.webank.wsdaw.keystore.service.KeystoreService;
import com.webank.wsdaw.keystore.vo.request.GenerateUserKeysRequest;
import com.webank.wsdaw.keystore.vo.request.GetUserKeysRequest;
import com.webank.wsdaw.keystore.vo.response.CommonResponse;
import com.webank.wsdaw.keystore.vo.response.GenerateUserKeysResponse;
import com.webank.wsdaw.keystore.vo.response.GetUserKeysResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/keystore")
@Slf4j
public class KeystoreController {

    @Autowired private KeystoreService keystoreService;

    @PostMapping("generateUserKeys")
    public CommonResponse<GenerateUserKeysResponse> generateUserKeys(
            @RequestBody @Valid GenerateUserKeysRequest request) throws Exception {
        return keystoreService.generateUserKeys(request);
    }

    @PostMapping("getUserKeys")
    public CommonResponse<GetUserKeysResponse> getUserKeys(
            @RequestBody @Valid GetUserKeysRequest request) throws Exception {
        return keystoreService.getUserKeys(request);
    }
}
