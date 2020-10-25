package net.jordimp.casino;

import org.apache.catalina.connector.Connector;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				connector.setProperty("relaxedQueryChars", "|{}[]");
			}
		});
		return factory;
	}
	
	@Bean
	public StorageProvider storageProvider(JobMapper jobMapper) {
	    InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
	    storageProvider.setJobMapper(jobMapper);
	    return storageProvider;
	}
	
	@Bean
	public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
	       return new BeanFactoryPostProcessor() {

	           @Override
	           public void postProcessBeanFactory(
	                   ConfigurableListableBeanFactory beanFactory) throws BeansException {
	               BeanDefinition bean = beanFactory.getBeanDefinition(
	                       DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

	               bean.getPropertyValues().add("loadOnStartup", 1);
	           }
	       };
	}

}
