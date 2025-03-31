package com.zack.aianswer.config;

<<<<<<< HEAD
import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiClientConfig {

    private String apiKey;

    @Bean
    public ClientV4 createClient() {
       return new ClientV4.Builder(apiKey).build();
    }
=======
public class AiClientConfig {
>>>>>>> 2b2c7a18520a61c296dce0034fb88bba0b093360
}
