package com.webank.wsdaw.keystore.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorUtil {

    private static final Logger MONILOGGER = LoggerFactory.getLogger("appmonitor");

    public static Logger getMonitorLogger() {
        return MONILOGGER;
    }
}
