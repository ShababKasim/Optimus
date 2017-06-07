package com.samaylabs.optimus.WebServices;

import javax.xml.ws.Endpoint;

public class OptimusWebService {

	public static void main(String[] args) {
		
		Thread.currentThread().setName("OptimusWebService");
		
		String ip = "localhost";
		
		String optimus = "http://" + ip +":9009/Optimus";
		String Track = "http://" + ip +":9009/Track";
		String Agv = "http://" + ip +":9009/Agv";
		String Log = "http://" + ip +":9009/Logs";
		
		OptimusService os = new OptimusService();
		TrackService ts = new TrackService();
		AgvService as = new AgvService();
		LogsService ls = new LogsService();
		
		Endpoint.publish(optimus, os);
		Endpoint.publish(Track, ts);
		Endpoint.publish(Agv, as);
		Endpoint.publish(Log, ls);
		
		System.out.println("Server started @ " + optimus + "?WSDL");
		System.out.println("Server started @ " + Track + "?WSDL");
		System.out.println("Server started @ " + Agv + "?WSDL");
		System.out.println("Server started @ " + Log + "?WSDL");
		
	}
	
	
}
