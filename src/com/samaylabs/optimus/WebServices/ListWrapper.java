package com.samaylabs.optimus.WebServices;

import java.util.ArrayList;
import java.util.List;

public class ListWrapper {

	private List<String> messages;

	ListWrapper(){
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
