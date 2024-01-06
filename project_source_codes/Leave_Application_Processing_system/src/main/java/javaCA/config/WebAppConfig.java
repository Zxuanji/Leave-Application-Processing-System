package javaCA.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javaCA.interceptor.SecurityInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer {
	@Autowired
	SecurityInterceptor securityInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registory) {
		registory.addInterceptor(securityInterceptor);
		
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registory.addInterceptor(localeChangeInterceptor);
	}
	
	@Bean
	LocaleResolver localeResolver() {
	return new CookieLocaleResolver();
	}
}
