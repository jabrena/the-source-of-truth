package org.jab.thesourceoftruth.config;

import org.jab.thesourceoftruth.service.git.Processor;
import org.jab.thesourceoftruth.service.git.ProcessorImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    @ConfigurationProperties(prefix = "config")
    GlobalConfiguration globalConfig(){
        return new GlobalConfiguration();
    }

    @Bean
    Processor processor(GlobalConfiguration config) {
        return new ProcessorImpl(config.getRepositories());
    }

}