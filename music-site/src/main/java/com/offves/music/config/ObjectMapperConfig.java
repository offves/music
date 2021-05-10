package com.offves.music.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.offves.music.common.util.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonUtil.getCommonObjectMapper();
    }

}
