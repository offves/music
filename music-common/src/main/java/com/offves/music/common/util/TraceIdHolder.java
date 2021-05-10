package com.offves.music.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;

import java.util.UUID;

public class TraceIdHolder {

    public final static String TRACE_ID = "trace_id";

    public static void initTrace() {
        if (StringUtils.isBlank(MDC.get(TRACE_ID))) {
            String traceId = generateTraceId();
            setTraceId(traceId);
        }
    }

    public static String getTrace() {
        return MDC.get(TRACE_ID);
    }

    public static void getTraceFromRpc(RpcContext context) {
        String traceId = context.getAttachment(TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            setTraceId(traceId);
        }
    }

    public static void putTraceId(RpcContext context) {
        String traceId = MDC.get(TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            context.setAttachment(TRACE_ID, traceId);
        }
    }

    public static void clearTrace() {
        MDC.clear();
    }

    private static void setTraceId(String traceId) {
        traceId = StringUtils.left(traceId, 36);
        MDC.put(TRACE_ID, traceId);
    }

    private static String generateTraceId() {
        return UUID.randomUUID().toString().toLowerCase().replaceAll("-", StringUtils.EMPTY);
    }

}
