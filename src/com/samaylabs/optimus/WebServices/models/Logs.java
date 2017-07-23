package com.samaylabs.optimus.WebServices.models;


/**
 * This is POJO class for Logs to Store and send data over Web Service
 * @author Tulve Shabab Kasim
 *
 */
public class Logs {

	private String userid;
	private String date;
	private String level;
	private String message;
	
	/**
	 * 
	 * @param userid Loggers user id
	 * @param date datetime stored along with logger
	 * @param level Debug, Info, Warning, Error
	 * @param message
	 */
	public Logs(String userid, String date, String level, String message) {
		super();
		this.userid = userid;
		this.date = date;
		this.level = level;
		this.message = message;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
