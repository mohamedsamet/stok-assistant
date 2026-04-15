package com.yesmind.stok.application.security.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("security.user")
public class UserProperties {

    private String login;
    private String salt;
    private String pwd;
    private String argonSalt;
    private String hashLength;
    private String parallelism;
    private String memory;
    private String iterations;

}
