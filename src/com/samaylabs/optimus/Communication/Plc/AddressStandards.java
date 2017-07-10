package com.samaylabs.optimus.Communication.Plc;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;

/**
 * 
 * @info This interface contains the declaration of all the Registers and Coils used to share date from or to Plc(Agv). 
 * @author Tulve Shabab Kasim
 * 
 */
public interface AddressStandards {
	
	/*D26*/ 	final int CURRENT_ANCHOR = 0x101A;
	/*D07*/ 	final int TYPE = 0x1007;
	/*D08*/	 	final int ACTION = 0x1008;
	/*M261*/ 	final int CMOVE = 0x905;
	
	/*D09*/ 	final int NEXT_ANCHOR = 0x1009;
	/*D11*/ 	final int NEXTTYPE = 0x1019;
	/*D770*/ 	final int NEXTACTION = 0x1302;
	/*M271*/ 	final int NMOVE = 0x915;
	
	/*D04*/ 	final int DROP_ANCHOR = 0x1004;
	/*M04*/ 	final int HOOKED = 0x804;
	
	/*M256*/ 	final int REACHED_ANCHOR = 0x900;
	/*M259*/ 	final int CHARGED = 0x903;
	/*M258*/ 	final int LOW_CHARGING = 0x902;
	
	/*M69*/ 	final int HEARTBEAT = 0x845 ;
	/*M60*/ 	final int HOOKINGBECON = 0x083C;
	
	/*D40*/ 	final int INFODEST = 0x1028;
	
	/*M55*/ 	final int RESUME = 0x837;
	/*M56*/ 	final int ABORT = 0x838;
	/*D41*/ 	final int CHANGESCREEN = 0x1029;
	
	/*M257*/ 	final int ERROR = 0x901;
	/*T30*/ 	final int OBSTACLE = 0x61E;
	/*T10*/	 	final int OUT_OF_LINE = 0x60A;	
	
	/*M200*/	final int RESET_AGV = 0x8C8;
	
	
	
	/**
	 * 
	 * @return current anchor_id of agv 
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	long getCurrentAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @param anchor value
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setCurrentAnchor(long value) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @param type of node i.e Split merge turn
	 * @param action to be taken on that node
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setCurrentTypeAction(int type, int action) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @param value true if move else false
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setCurrentMove(boolean value) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return next anchor_id of agv 
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	long getNextAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @param value
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setNextAnchor(long value) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 *@param type of node i.e Split merge turn
	 * @param action to be taken on that node
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setNextTypeAction(int type, int action) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @param value true if move else false
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setNextMove(boolean value) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return true if agv is open to listen commands from server
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean reachedAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @param value to make agv move
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setReachedAnchor(boolean value) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return current anchor_id of agv 
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	long dropAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 *  
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void resetDropAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void startHooking() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void stopHooking() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return true if trolley is hooked to agv
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean isHooked() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return returns true if agv is charging
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean isCharged() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return returns true if charging is low
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean lowCharging() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @param value
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setInfoDest(int value) throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @param value
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void resetInfoDest() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void setHeartbeat() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @return if there is any error of all nature
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean isResume() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @return if there is any error of all nature
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean isAbort() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void pauseScreen() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void resetPauseScreen() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @return if there is any error of all nature
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean isError() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return true if obstacle found
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean isObstacle() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * 
	 * @return true if Agv is out of line found
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	boolean isOutOfLine() throws ModbusIOException, ModbusSlaveException, ModbusException;

	/**
	 * @throws ModbusIOException
	 * @throws ModbusSlaveException
	 * @throws ModbusException
	 */
	void resetAgv() throws ModbusIOException, ModbusSlaveException, ModbusException;
}
