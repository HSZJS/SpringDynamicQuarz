package com.cpic.insurance.auap.base.quarz.common;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	private SpringApplicationContext() {

	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <O> O getBean(String name, Class<O> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public static <O> O getBean(Class<O> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static <O> String[] getBeanNamesForType(Class<O> requiredType) {
		return applicationContext.getBeanNamesForType(requiredType);
	}

	public static <O> Map<String, O> getBeansForType(Class<O> requiredType) {
		return applicationContext.getBeansOfType(requiredType);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringApplicationContext.applicationContext = applicationContext;
	}
}
