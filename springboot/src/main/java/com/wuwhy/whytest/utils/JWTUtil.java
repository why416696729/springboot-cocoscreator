package com.wuwhy.whytest.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "audience")
@Component
public class JWTUtil {

        private String clientId;
        private String base64Secret;
        private String name;
        private int expiresSecond;
}
