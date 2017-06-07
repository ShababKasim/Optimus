package com.samaylabs.optimus.Communication.Plc;

import java.net.UnknownHostException;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;

/**
 * @info This class is implementation of AddressStandards about setting getting or resetting of shared Coils or Registers with the Plc(Agv)
 * @author Tulve Shabab Kasim
 *
 */
public class AgvMethods extends PlcMethods implements AddressStandards{

	/**
	 * 
	 * @param ipaddress of an Agv
	 * @param port op Agv
	 * @throws UnknownHostException
	 */
	public AgvMethods(String ipaddress, int port) throws UnknownHostException {
		super(ipaddress, port);
	}

	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#getCurrentAnchor()
	 */
	@Override
	public long getCurrentAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return Long.parseLong(Integer.toString(readRegister(CURRENT_ANCHOR)));
		//return Long.parseLong(Integer.toString(readRegister(0x1007)) + Integer.toString(readRegister(0x1006)));
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setCurrentAnchor(long)
	 */
	@Override
	public void setCurrentAnchor(long value) throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeRegister(CURRENT_ANCHOR, (int) value);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setCurrentTypeAction(int, int)
	 */
	@Override
	public void setCurrentTypeAction(int type, int action) throws ModbusIOException, ModbusSlaveException, ModbusException{
		writeRegister(TYPE, type);
		writeRegister(ACTION, action);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setCurrentMove(boolean)
	 */
	@Override
	public void setCurrentMove(boolean value) throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeCoil(CMOVE,value);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#getNextAnchor()
	 */
	@Override
	public long getNextAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return Long.parseLong(Integer.toString(readRegister(NEXT_ANCHOR)));
		//return Long.parseLong(Integer.toString(readRegister(0x1007)) + Integer.toString(readRegister(0x1006)));
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setNextAnchor(long)
	 */
	@Override
	public void setNextAnchor(long value) throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeRegister(NEXT_ANCHOR, (int) value);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setNextTypeAction(int, int)
	 */
	@Override
	public void setNextTypeAction(int type, int action) throws ModbusIOException, ModbusSlaveException, ModbusException{
		writeRegister(NEXTTYPE, type);
		writeRegister(NEXTACTION, action);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setNextMove(boolean)
	 */
	@Override
	public void setNextMove(boolean value) throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeCoil(NMOVE,value);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#reachedAnchor()
	 */
	@Override
	public boolean reachedAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return (readCoil(REACHED_ANCHOR) == 0) ? false : true;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setReachedAnchor(boolean)
	 */
	@Override
	public void setReachedAnchor(boolean value) throws ModbusIOException, ModbusSlaveException, ModbusException{
		writeCoil(REACHED_ANCHOR , value);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#dropAnchor()
	 */
	@Override
	public long dropAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return Long.parseLong(Integer.toString(readRegister(DROP_ANCHOR)));
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#resetDropAnchor()
	 */
	@Override
	public void resetDropAnchor() throws ModbusIOException, ModbusSlaveException, ModbusException{
		writeRegister(DROP_ANCHOR, 0);
		writeCoil(HOOKED,false);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#startHooking()
	 */
	@Override
	public void startHooking() throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeCoil(HOOKINGBECON,true);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#stopHooking()
	 */
	@Override
	public void stopHooking() throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeCoil(HOOKINGBECON,false);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#isHooked()
	 */
	@Override
	public boolean isHooked() throws ModbusIOException, ModbusSlaveException, ModbusException {
		return (readCoil(HOOKED) == 1) ? true : false;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#isCharged()
	 */
	@Override
	public boolean isCharged() throws ModbusIOException, ModbusSlaveException, ModbusException {
		return (readCoil(CHARGED) == 1) ? true : false;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#lowCharging()
	 */
	@Override
	public boolean lowCharging() throws ModbusIOException, ModbusSlaveException, ModbusException {
		return (readCoil(LOW_CHARGING) == 1) ? true : false;
	}
		
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setInfoDest(int)
	 */
	@Override
	public void setInfoDest(int value) throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeRegister(INFODEST, value);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#resetInfoDest()
	 */
	@Override
	public void resetInfoDest() throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeRegister(INFODEST, 0);
	}

	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#setHeartbeat()
	 */
	@Override
	public void setHeartbeat() throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeCoil(HEARTBEAT,true);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#isResume()
	 */
	@Override
	public boolean isResume() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return (readCoil(RESUME) == 0) ? false : true;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#isAbort()
	 */
	@Override
	public boolean isAbort() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return (readCoil(ABORT) == 0) ? false : true;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#pauseScreen()
	 */
	@Override
	public void pauseScreen() throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeRegister(CHANGESCREEN,3);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#resetPauseScreen()
	 */
	@Override
	public void resetPauseScreen() throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeRegister(CHANGESCREEN,0);
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#isError()
	 */
	@Override
	public boolean isError() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return (readCoil(ERROR) == 0) ? false : true;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#isObstacle()
	 */
	@Override
	public boolean isObstacle() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return (readCoil(OBSTACLE) == 0) ? false : true;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#isOutOfLine()
	 */
	@Override
	public boolean isOutOfLine() throws ModbusIOException, ModbusSlaveException, ModbusException{
		return (readCoil(OUT_OF_LINE) == 0) ? false : true;
	}
	
	/* (non-Javadoc)
	 * @see com.samaylabs.optimus.Communication.Plc.AddressStandards#resetAgv()
	 */
	@Override
	public void resetAgv() throws ModbusIOException, ModbusSlaveException, ModbusException {
		writeCoil(RESET_AGV,true);
	}
	
}

