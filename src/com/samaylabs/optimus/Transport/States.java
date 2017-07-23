package com.samaylabs.optimus.Transport;


/**
 * This is Enum which is used in Agv StateMachine to switch between states.
 * @author Tulve Shabab Kasim
 *
 */
public enum States {

	Idle,
	
	TravelToPark,
	
	Pickup,
	Drop,
	Charging,
	TravelToCharge,
	
	Pause,
	Connection,
	
	Notification
	
	
	
	
}
