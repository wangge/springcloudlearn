package com.learn.eurekafeignuploadclient;

import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultpartSupportConfig {

    @Bean
    public SpringFormEncoder feignFormEncorder(){
        return new SpringFormEncoder();
    }
}
