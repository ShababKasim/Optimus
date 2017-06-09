package com.samaylabs.optimus.Dao;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PropertyConfigurator;

public class DbLogger {

	public Logger getLogger(String user){
		Logger log = Logger.getLogger("Optimus");
		PropertyConfigurator.configure("log4j.properties");
		MDC.put("user", user); 
		return log;
	}
	
}
