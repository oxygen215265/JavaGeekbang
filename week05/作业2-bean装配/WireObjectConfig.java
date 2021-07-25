package com.htx.schoolstarter.auto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WireObjectConfig {

    @Bean(name="byConfiguration")
    public WireObject myWireObject() {
        return new WireObject();
    }


}
