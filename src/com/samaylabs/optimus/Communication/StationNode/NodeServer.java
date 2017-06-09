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

    public NodeServer(int port ,  List<Ticket> queue,AtomicInteger ticketCount){
        this.serverPort = port;
        this.queue = queue;
        this.ticketCount = ticketCount;
        nodeWorkers = new ArrayList<NodeWorker>();
    }
    
    public NodeServer(int port ,  List<Ticket> queue,AtomicInteger ticketCount, List<NodeWorker> nodeWorkers){
        this.serverPort = port;
        this.queue = queue;
        this.ticketCount = ticketCount;
        this.nodeWorkers = nodeWorkers;
    }

    public void run(){
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            NodeWorker nw = new NodeWorker(clientSocket,queue,ticketCount,nodeWorkers);
            new Thread(nw).start();
        }
        System.out.println("Server Stopped.") ;
    }

    public synchronized boolean isStopped() {
        return this.isStopped;
    }

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
    }

    public List<NodeWorker> getNodeWorker() {
		return nodeWorkers;
	}

	public void setNodeWorker(List<NodeWorker> nodeWorker) {
		this.nodeWorkers = nodeWorker;
	}

	private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

	public List<Ticket> getQueue() {
		return queue;
	}

	public void setQueue(List<Ticket> queue) {
		this.queue = queue;
	}

}
