package com.offves.music.common.conf;

import com.offves.music.common.filter.HttpTraceLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
public class HttpTraceConfiguration {

    @Bean
    public HttpTraceLogFilter httpTraceLogFilter() {
        return new HttpTraceLogFilter();
    }

}