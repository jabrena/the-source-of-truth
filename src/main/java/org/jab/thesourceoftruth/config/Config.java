package org.jab.thesourceoftruth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    @ConfigurationProperties(prefix = "artifacts")
    GlobalConfiguration globalConfig(){
        return new GlobalConfiguration();
    }

}