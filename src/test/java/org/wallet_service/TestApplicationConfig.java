package org.wallet_service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.wallet_service.config.SpringConfig;
import org.wallet_service.config.WebConfig;

/**
 * Spring launcher for Tests
 */
@Configuration
@ComponentScan(
        value = "org.wallet_service"
//        excludeFilters = {@ComponentScan.Filter(
//                value = {WebConfig.class, SpringConfig.class},
//                type = FilterType.ASSIGNABLE_TYPE)
//        }
)
public class TestApplicationConfig {
}
