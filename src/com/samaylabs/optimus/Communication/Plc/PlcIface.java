package com.samaylabs.optimus.Communication.Plc;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;

/**
 * @Info interface for modbus read/write methods 
 * @author Tulve Shabab Kasim
 *
 */
public interface PlcIface {

	public int readRegister(int start) throws ModbusIOException, ModbusSlaveException, ModbusException;
	
	public void writeRegister(int regaddr, int value) throws ModbusIOException, ModbusSlaveException, ModbusException;
	public void writeRegisters();
	
	public int readCoils(int start ,int end) throws ModbusIOException, ModbusSlaveException, ModbusException;
	public int readCoil(int start) throws ModbusIOException, ModbusSlaveException, ModbusException;
	
	public void writeCoil(int coiladdr ,boolean status) throws ModbusIOException, ModbusSlaveException, ModbusException;
	public void writeCoils();

	
}
