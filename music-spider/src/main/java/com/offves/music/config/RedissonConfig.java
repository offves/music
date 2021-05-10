package com.offves.music.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(RedisClusterProperties redisClusterProperties) {
        Config config = new Config();
        String[] nodeAddresses = redisClusterProperties.getCluster().getNodes().stream().map(p -> "redis://" + p).toArray(String[]::new);
        ClusterServersConfig clusterServersConfig = config.useClusterServers().addNodeAddress(nodeAddresses).setPassword(redisClusterProperties.getPassword());
        return Redisson.create(config);
    }


}
