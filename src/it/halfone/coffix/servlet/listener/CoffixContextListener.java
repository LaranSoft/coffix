package it.halfone.coffix.servlet.listener;

import it.halfone.coffix.configuration.Configuration;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CoffixContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent ctx) {
		Configuration.getInstance().clear();
	}

	@Override
	public void contextInitialized(ServletContextEvent ctx) {
		System.out.println("initializing context");
		ResourceBundle bundle = ResourceBundle.getBundle("it.halfone.coffix.bundle.MessagesBundle", Locale.getDefault());
		
		Enumeration<String> keys = bundle.getKeys(); 
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			Configuration.getInstance().put(key, bundle.getString(key));
		}
		
		System.out.println("context initialized");
	}

}
