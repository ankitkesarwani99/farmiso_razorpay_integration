package com.farmiso.razorpayintegration.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class RazorpayClientConfig {

    @Value("${razorpay.client.key-id}")
    private String keyId;

    @Value("${razorpay.client.secret-key}")
    private String secretKey;

    @Bean
    public RazorpayClient getRazorPayClient() throws RazorpayException {
        return new RazorpayClient(keyId,secretKey);
    }
}

