package com.hzq.config;

import com.xxl.conf.core.spring.XxlConfFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jimmy Shan
 * @date 2021-11-05
 * @desc XXL-CONFIG 配置类
 */
@Configuration
public class XxlConfig {
    @Value("${xxl.config.address}")
    private String adminAddress;
    @Value("${xxl.config.env}")
    private String env;
    @Value("${xxl.config.token}")
    private String accessToken;
    @Value("${xxl.config.mirrorfile}")
    private String mirrorFile;

    @Bean
    public XxlConfFactory xxlConfFactory() {
        XxlConfFactory xxlConf = new XxlConfFactory();
        xxlConf.setAdminAddress(adminAddress);
        xxlConf.setEnv(env);
        xxlConf.setAccessToken(accessToken);
        xxlConf.setMirrorfile(mirrorFile);
        return xxlConf;
    }
}
