package com.example.jms.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class HandlerLookup implements ApplicationContextAware {
	private ApplicationContext				applicationContext;
	private volatile static HandlerLookup	instance	= null;

	/**
	 * Constructor is private to prevent any other object from instantiating it.
	 */
	private HandlerLookup() {

	}

	public static HandlerLookup getInstance() {
		if(instance == null) {
			synchronized(HandlerLookup.class) {
				instance = new HandlerLookup();
			}
		}

		return instance;
	}

	public synchronized Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
