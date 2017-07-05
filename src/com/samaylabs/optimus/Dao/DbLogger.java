package com.samaylabs.optimus.Dao;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author Tulve Shabab Kasim
 *
 */
public class DbLogger {

	/**
	 * 
	 * @param user
	 * @return Logger object suct that all logs pushed in that name
	 */
	public Logger getLogger(String user){
		Logger log = Logger.getLogger("Optimus");
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		MDC.put("user", user); 
		return log;
	}

}
