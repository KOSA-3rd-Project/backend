package com.dtbid.dropthebid.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/static/**")
              .addResourceLocations("classpath:/static/");
      registry.addResourceHandler("/public/**")
              .addResourceLocations("classpath:/public/");
      registry.addResourceHandler("/resources/**")
              .addResourceLocations("classpath:/resources/");
      registry.addResourceHandler("/webjars/**")
              .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
