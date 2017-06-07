package com.samaylabs.optimus.Communication.Plc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 * @info This class is implementation of methods used for communication via modbus.
 * @author Tulve Shabab Kasim
 *
 */
public class PlcMethods implements PlcIface {

	TCPMasterConnection con = null; //the connection
	ModbusTCPTransaction trans = null; //the transaction
	ReadInputDiscretesRequest req = null; //the request
	ReadInputDiscretesResponse res = null; //the response
	private String ip;
	private int port;

	
	public PlcMethods( String ip, int port ) throws UnknownHostException {
		this.ip = ip;
		this.port = port;
	}

	public void connect() throws ModbusException , UnknownHostException, Exception {
		con = new TCPMasterConnection(InetAddress.getByName(ip));
		con.setPort(port);
		con.connect();
//		con.setTimeout(30000);
	}
	
	public void close(){
		con.close();
	}

	public int readCoils(int start, int end) throws ModbusIOException, ModbusSlaveException, ModbusException {

		req = new ReadInputDiscretesRequest(start, end);

		trans = new ModbusTCPTransaction(con);
		trans.setRequest(req);

		trans.execute();
		res = (ReadInputDiscretesResponse) trans.getResponse();
		String s = res.getDiscretes().toString();
		return Integer.parseInt(s.substring(0, end-1));

	}

	public int readCoil(int start) throws ModbusIOException, ModbusSlaveException, ModbusException {
		req = new ReadInputDiscretesRequest(start,1);

		trans = new ModbusTCPTransaction(con);
		trans.setRequest(req);

		
		trans.execute();
		res =  (ReadInputDiscretesResponse) trans.getResponse();
		String s = res.getDiscretes().toString();
		return Integer.parseInt(s.substring(0,1));
	}

	public int readRegister(int start) throws ModbusIOException, ModbusSlaveException, ModbusException {

		ReadMultipleRegistersRequest Rreq = new ReadMultipleRegistersRequest(start ,1);
		ReadMultipleRegistersResponse Rres = new ReadMultipleRegistersResponse();

		Rreq.setUnitID(1); 
		Rres.setUnitID(1);

		trans = new ModbusTCPTransaction(con);
		trans.setRequest(Rreq);

		trans.execute();
		Rres = (ReadMultipleRegistersResponse) trans.getResponse();

		//		System.out.println("Start Register..> " + Integer.toHexString(start));
		//		int count=1;
		//		for (int k=0 ; k<count ; k++){
		//			System.out.println("The value READ: " + Rres.getRegisterValue(k));

		return Rres.getRegisterValue(0);

	}

	public void writeCoil(int coiladdr ,boolean status) throws ModbusIOException, ModbusSlaveException, ModbusException {

		trans = new ModbusTCPTransaction(con);

		WriteCoilRequest wcr = new WriteCoilRequest();
		wcr.setUnitID(1);
		wcr.setReference(coiladdr);

		wcr.setCoil(status);

		trans.setRequest(wcr);

		trans.execute();
//		WriteCoilResponse res = (WriteCoilResponse) trans.getResponse();

		/*if (res != null) {
			System.out.println("Set coil " + res.getReference() + " to " + res.getCoil());
		}*/

	}

	public void writeCoils() {
		// TODO Yet to be completed

	}

	public void writeRegister(int regaddr, int value) throws ModbusIOException, ModbusSlaveException, ModbusException {

		WriteSingleRegisterRequest WriteReq = null; 
		SimpleRegister MyReg = new SimpleRegister(1);

		//3. Prepare the request
		WriteReq = new WriteSingleRegisterRequest();
		WriteReq.setReference(regaddr);  //register number
		MyReg.setValue(value);         //value for register
		WriteReq.setRegister(MyReg);

		//4. Prepare the transaction

		trans = new ModbusTCPTransaction(con);
		trans.setRequest(WriteReq);

		trans.execute();

	}

	public void writeRegisters() {


	}


}


