package com.webank.wsdaw.keystore.service;

import com.webank.wsdaw.keystore.vo.request.GenerateUserKeysRequest;
import com.webank.wsdaw.keystore.vo.request.GetUserKeysRequest;
import com.webank.wsdaw.keystore.vo.response.CommonResponse;
import com.webank.wsdaw.keystore.vo.response.GenerateUserKeysResponse;
import com.webank.wsdaw.keystore.vo.response.GetUserKeysResponse;

public interface KeystoreService {

    CommonResponse<GenerateUserKeysResponse> generateUserKeys(GenerateUserKeysRequest request);

    CommonResponse<GetUserKeysResponse> getUserKeys(GetUserKeysRequest request);
}
