package com.offves.music.config;

import io.rebloom.client.ClusterClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class RedisBloomConfig {

    @Bean
    public ClusterClient clusterClient(RedisClusterProperties redisClusterProperties) {
        Set<HostAndPort> jedisClusterNodes = redisClusterProperties.getCluster().getNodes().stream()
                .map(p -> new HostAndPort(p.split(":")[0], Integer.parseInt(p.split(":")[1]))).collect(Collectors.toSet());
        GenericObjectPoolConfig<String> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(500);
        poolConfig.setMinIdle(100);
        poolConfig.setMaxWaitMillis(500);
        poolConfig.setMaxTotal(1500);
        return new ClusterClient(jedisClusterNodes, 5000, 60000, 5, redisClusterProperties.getPassword(), poolConfig);
    }

}
