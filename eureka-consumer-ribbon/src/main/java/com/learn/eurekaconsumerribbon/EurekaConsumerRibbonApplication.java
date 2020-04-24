package com.learn.eurekaconsumerribbon;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@RestController
@SpringBootApplication
@EnableHystrix
public class EurekaConsumerRibbonApplication {

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    /**
     * 当我们在 RestTemplate 上添加 @LoadBalanced 注解后，就可以用服务名称来调用接口了，当有多个服务的时候，还能做负载均衡
     * 如使用 服务器名：eureka-client
     * 如果eureka-client无法请求，将调用fallback()方法，进行服务降级（依赖隔离）
     */
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @GetMapping("dc")
    @HystrixCommand(fallbackMethod = "fallback")
    public String dc(){
        String url = "http://eureka-client/dc";
        System.out.println(url);
        return restTemplate.getForObject(url, String.class);
    }

    public String fallback(){
        return "fallback";
    }
}
