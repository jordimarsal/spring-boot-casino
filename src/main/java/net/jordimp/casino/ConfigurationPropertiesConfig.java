package net.jordimp.casino;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({ @PropertySource("classpath:conf.properties") })
public class ConfigurationPropertiesConfig {

}
