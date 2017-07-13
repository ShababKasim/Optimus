package com.samaylabs.optimus.WebServices.models;

import java.util.Random;

public class Test {

	public static void main(String[] args) {
		
		Random rand = new Random();
		for(int i=1;i<=10;i++){
			System.out.println("insert into utilization values(1,\"2017-07-"+i+"\"," + rand.nextInt(500)+","+rand.nextInt(900)+","+rand.nextInt(1200)+","+rand.nextInt(40)+","+rand.nextInt(200)+");");
		}
		
		
	}
	
}
