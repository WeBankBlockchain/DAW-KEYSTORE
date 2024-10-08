package com.webank.wsdaw.keystore.dao.mapper;

import com.webank.wsdaw.keystore.dao.entity.KeystoreEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface KeystoreMapper {

    @Insert(
            "INSERT INTO t_keystore_info (wework_uid, public_key, private_key) values(#{weworkUid},#{publicKey}, #{privateKey})")
    @Options(useGeneratedKeys = true, keyProperty = "pkId", keyColumn = "pk_id")
    int insertUserKeys(KeystoreEntity entity);

    @Select("SELECT * FROM t_keystore_info WHERE wework_uid=#{weworkUid}")
    @ResultType(KeystoreEntity.class)
    KeystoreEntity getUserKeys(KeystoreEntity entity);
}
