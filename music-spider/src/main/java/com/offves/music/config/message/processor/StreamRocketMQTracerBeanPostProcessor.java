package com.offves.music.config.message.processor;

import com.alipay.common.tracer.core.configuration.SofaTracerConfiguration;
import com.alipay.sofa.tracer.plugins.message.interceptor.SofaTracerChannelInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.lang.NonNull;

public class StreamRocketMQTracerBeanPostProcessor implements BeanPostProcessor, EnvironmentAware, PriorityOrdered {

    private Environment environment;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof AbstractMessageChannel) {
            String appName = environment.getProperty(SofaTracerConfiguration.TRACER_APPNAME_KEY);
            ((AbstractMessageChannel) bean).addInterceptor(SofaTracerChannelInterceptor.create(appName));
        }
        return bean;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
