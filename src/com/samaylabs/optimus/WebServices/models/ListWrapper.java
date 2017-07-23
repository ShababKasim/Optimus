package com.samaylabs.optimus.WebServices.models;

import java.util.ArrayList;
import java.util.List;


/**
 * This is Wrapper Class created to store List of Type String in order to transfer over webService 
 * @author Tulve Shabab Kasim
 *
 */
public class ListWrapper {

	private List<String> messages;

	public ListWrapper(){
		messages = new ArrayList<String>();
	}
	
	public ListWrapper(List<String> messages){
		this.messages = messages;
	}
	
	public void add(String message){
		this.messages.add(message);
	}
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	
}
