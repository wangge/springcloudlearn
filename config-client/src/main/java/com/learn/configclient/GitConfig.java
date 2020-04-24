package com.learn.configclient;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data  //@Data : 注解在类上, 为类提供读写属性, 此外还提供了 equals()、hashCode()、toString() 方法
public class GitConfig {

    @Value("${data.env}") //git 配置文件的值,且配置文件只能用bootstrap.yml,如果用application.yml是获取不到的。
    private String env;

    @Value("${data.user.username}")
    private String username;

    @Value("${data.user.password}")
    private String password;
}
