package com.offves.music.common.filter;

import com.alipay.common.tracer.core.context.trace.SofaTraceContext;
import com.alipay.common.tracer.core.holder.SofaTraceContextHolder;
import com.offves.music.common.dto.Response;
import com.offves.music.common.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Slf4j
@Activate(order = 100, group = {PROVIDER, CONSUMER})
public class DubboTraceLogFilter implements Filter {

    @Override
    @SuppressWarnings("rawtypes")
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String interfaceName = invocation.getInvoker().getInterface().getSimpleName();
        String methodName = invocation.getMethodName();
        Object[] args = getArgs(invocation);

        long startTime = System.currentTimeMillis();

        Result result = invoker.invoke(invocation);

        long timeConsuming = System.currentTimeMillis() - startTime;

        boolean hasException = result.hasException();

        SofaTraceContext sofaTraceContext = SofaTraceContextHolder.getSofaTraceContext();
        Object value = result.getValue();
        if (value instanceof Response && Objects.nonNull(sofaTraceContext) && Objects.nonNull(sofaTraceContext.getCurrentSpan()) && Objects.nonNull(sofaTraceContext.getCurrentSpan().getSofaTracerSpanContext())) {
            Response response = (Response) value;
            response.setTraceId(sofaTraceContext.getCurrentSpan().getSofaTracerSpanContext().getTraceId());
        }

        boolean providerSide = RpcContext.getContext().isProviderSide();

        DubboTraceLog dubboTraceLog = new DubboTraceLog(providerSide ? "provider" : "consumer" + StringUtils.SPACE + interfaceName + "." + methodName, timeConsuming, args, value, hasException, hasException ? result.getException() : null);

        log.info("Dubbo Trace Log. " + JsonUtil.toString(dubboTraceLog));

        if (hasException && invoker.getInterface() != GenericService.class) {
            log.error("invoker {}, error. {}", dubboTraceLog.getMethod(), result.getException());
        }

        return result;
    }

    private Object[] getArgs(Invocation invocation) {
        return Arrays.stream(invocation.getArguments()).filter(arg -> !(arg instanceof byte[]) && !(arg instanceof Byte[]) && !(arg instanceof InputStream) && !(arg instanceof File)).toArray();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DubboTraceLog {

        private String method;

        private Long timeConsuming;

        private Object[] request;

        private Object response;

        private Boolean hasException;

        private Throwable throwable;

    }

}