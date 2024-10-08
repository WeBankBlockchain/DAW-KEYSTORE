package com.webank.wsdaw.keystore.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class MonitorEntry {
    private String code;
    private String bizSeqNo;
    private String resCode;
    private String message;
    private long usedTime;
}
