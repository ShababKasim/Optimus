package com.samaylabs.optimus.Communication.StationNode;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.samaylabs.optimus.Dao.TicketDao;

/**
 * 
 * @Info This class is Worker Thread Class which holds a Station Node Session used for rising Ticket.
 * @author Tulve Shabab Kasim
 *
 */
public class NodeWorker implements Runnable{

	private Socket clientSocket = null;

	private List<NodeWorker> nodeWorkers;
	private final List<Ticket> queue;
	private boolean stop;
	volatile private String inputreq;
	volatile private Long tUid;

	private AtomicInteger ticketCount;
	protected BufferedReader input;
	protected DataOutputStream output;
	protected Date date;
	protected SimpleDateFormat formatter;

	

	public NodeWorker(Socket clientSocket, List<Ticket> queue,AtomicInteger ticketCount, List<NodeWorker> nodeWorkers) {
		this.clientSocket = clientSocket;
		this.ticketCount = ticketCount;
		this.queue = queue;
		this.nodeWorkers = nodeWorkers;

		try {
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopWorker(){
		stop = true;
	}
	
	public String getInputreq() {
		return inputreq;
	}

	public void setInputreq(String inputreq) {
		this.inputreq = inputreq;
	}

	public Long gettUid() {
		return tUid;
	}

	public void settUid(Long tUid) {
		this.tUid = tUid;
	}

	public void run() {
		try {
			String nodeId;
			String check = "1AF37";
			if(input.readLine().equals(check)){
				nodeId = input.readLine();
				Thread.currentThread().setName("Node-> " +nodeId);
				nodeWorkers.add(this);
//				System.out.println("Connected to Client" + nodeId);
				
				clientSocket.setSoTimeout(5000);
	
				inputreq = "alive";
				
				while(!stop) {

					date = new Date();
					formatter = new SimpleDateFormat ("yyyyMMddhhmmss");
					
					switch(inputreq) {

					case "alive":
//     						System.out.println("Alive");
						output.writeBytes("alive}");
						inputreq = input.readLine();
//						System.out.println("sent.>alive; rec..>" + inputreq);
						break;
					
					case "booked":
//						System.out.println("booked");
						output.writeBytes("booked}");
						inputreq = input.readLine();
//						System.out.println("sent.>alive; rec..>" + inputreq);
						break;	
						
					case "create":
//						System.out.println("in CT..>" + inputreq);
						tUid = Long.parseLong(formatter.format(date) + nodeId);
						try{
							Ticket ticket = new Ticket(ticketCount.incrementAndGet(),tUid,Integer.parseInt(nodeId),"Pickup","Unalloted"); 
							queue.add(ticket);
							new TicketDao().insertTicket(ticket);
							output.flush();
							output.writeBytes("success}");
							inputreq = "booked";
//							System.out.println("Got a Ticket");
						} catch (ConcurrentModificationException e) {
							output.writeBytes("fail}");
							inputreq = "alive";
//							System.out.println("Exeption getting a Ticket");
						}
						
						break;
					
					case "serving":
//						System.out.println("Serving");
						output.writeBytes("serving}");
						inputreq = input.readLine();
//						System.out.println("sent.>serving; rec..>" + inputreq);
						break;
						
					case "drop":
//						System.out.println("drop");
						output.writeBytes("drop}");	
						inputreq = "alive";
//						System.out.println("sent.>drop; rec..>" + inputreq);
						break;	
					
					default:
//						System.out.println("Default");
					
	}
				}
			}
			Thread.sleep(400);
		} catch (IOException | InterruptedException | NullPointerException e) {
			nodeWorkers.remove(this);
		} finally {
//			System.out.println("disconnected" );
			try {
				output.close();
				input.close();
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}       
	}
}

