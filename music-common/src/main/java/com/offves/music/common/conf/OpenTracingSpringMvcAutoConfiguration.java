package com.offves.music.common.conf;

import com.alipay.sofa.tracer.plugins.springmvc.SpringMvcSofaTracerFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "com.alipay.sofa.tracer.springmvc", value = "enable", matchIfMissing = true)
public class OpenTracingSpringMvcAutoConfiguration {

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public FilterRegistrationBean springMvcDelegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        SpringMvcSofaTracerFilter filter = new SpringMvcSofaTracerFilter();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName(filter.getFilterName());
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

}