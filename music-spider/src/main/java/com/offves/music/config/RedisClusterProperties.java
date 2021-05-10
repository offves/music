package com.offves.music.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisClusterProperties {

    private String password;

    private cluster cluster;

    public static class cluster {

        private List<String> nodes;

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RedisClusterProperties.cluster getCluster() {
        return cluster;
    }

    public void setCluster(RedisClusterProperties.cluster cluster) {
        this.cluster = cluster;
    }

}
