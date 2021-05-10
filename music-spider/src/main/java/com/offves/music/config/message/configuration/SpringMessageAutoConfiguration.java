package com.offves.music.config.message.configuration;

import com.offves.music.config.message.processor.StreamRocketMQTracerBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.messaging.DirectWithAttributesChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
@ConditionalOnClass({AbstractMessageChannel.class, ChannelInterceptor.class, DirectWithAttributesChannel.class})
@ConditionalOnProperty(prefix = "com.alipay.sofa.tracer.message", value = "enable", matchIfMissing = true)
public class SpringMessageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StreamRocketMQTracerBeanPostProcessor streamRocketMQTracerBeanPostProcessor() {
        return new StreamRocketMQTracerBeanPostProcessor();
    }

}
