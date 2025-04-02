package com.zack.aianswer.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissionConfig {
    private String host;
    private int port;
    private String password;
    private int database;

    @Bean
    public RedissonClient redissionClient(){
         Config config=new Config();
          config.useSingleServer()
                  .setAddress("redis://"+host+":"+port)
                  .setDatabase(database);

        return Redisson.create(config);
    }
}
