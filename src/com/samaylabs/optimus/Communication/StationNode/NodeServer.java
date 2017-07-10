package com.samaylabs.optimus.Communication.StationNode;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.samaylabs.optimus.Communication.StationNode.models.Ticket;

/**
 * 
 * @Info This class is used to create server for all the push station buttons for calling an Agv
 * @author Tulve Shabab Kasim
 *
 */
public class NodeServer extends Thread{

   
	private int          serverPort   = 8080;
    private ServerSocket serverSocket = null;
    private boolean      isStopped    = false;
    private List<NodeWorker> nodeWorkers = null;
    private List<Ticket> queue;
    private AtomicInteger ticketCount;

    /**
     * @info Constructor without nodeworkers array 
     * @param port on which server needs to start
     * @param queue for holding tickets
     * @param ticketCount atomic ticket count 
     */
    public NodeServer(int port ,  List<Ticket> queue,AtomicInteger ticketCount){
    	super("Node Server");
        this.serverPort = port;
        this.queue = queue;
        this.ticketCount = ticketCount;
        nodeWorkers = new ArrayList<NodeWorker>();
    }
    
    
    /**
     * @info Constructor without nodeworkers array
     * @param port on which server needs to start
     * @param queue for holding tickets
     * @param ticketCount atomic ticket count
     * @param nodeWorkers array for holding connected nodes
     */
    public NodeServer(int port ,  List<Ticket> queue,AtomicInteger ticketCount, List<NodeWorker> nodeWorkers){
    	super("Node Server");
    	this.serverPort = port;
        this.queue = queue;
        this.ticketCount = ticketCount;
        this.nodeWorkers = nodeWorkers;
    }

    
    /**
     * Opens a port and waits for connection, once got a connection creates saperate thread to handle request response
     */
    public void run(){
        openServerSocket();
        while(!isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.");
                    this.interrupt();
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new NodeWorker(clientSocket,queue,ticketCount,nodeWorkers)).start();
        }
        System.out.println("Server Stopped.") ;
    }

    /**
     * Check if server is stopped
     * @return true if stopped
     */
    public synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Stops server and closes port, closes all connection established by nodeworkers.
     */
    public synchronized void stopServer(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
        for(NodeWorker n : nodeWorkers ){
        	n.stopWorker();
        }
        this.interrupt();
    }

    /**
     * 
     * @return array of node worker instances
     */
    public List<NodeWorker> getNodeWorker() {
		return nodeWorkers;
	}

    
    /**
     * 
     * @param nodeWorker 
     */
	public void setNodeWorker(List<NodeWorker> nodeWorker) {
		this.nodeWorkers = nodeWorker;
	}

	/**
	 * Opens port for node server
	 */
	private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

	/**
	 * 
	 * @return queue of ticket
	 */
	public List<Ticket> getQueue() {
		return queue;
	}

	
	/**
	 * 
	 * @param queue sets to server instance
	 */
	public void setQueue(List<Ticket> queue) {
		this.queue = queue;
	}

}
