package com.samaylabs.optimus.Dao;

/**
 * 
 * @author Shabab
 *
 */
public interface DbConstants {
	
	final String DBNAME = "optimus";
	final String USERNAME = "developer";
	final String PASSWORD = "developer";
	final String HOST = "localhost:3306";
	
	final String selectnode = "select * from node";
	final String selectnodeResolver = "select * from noderesolver";
	final String selectedge = "select * from edge";
	
	final String insertnode = "insert into node values(?,?,?,?,?,?)";
	final String insertedge = "insert into edge values(?,(select anchor from node where node.n_name = ?),(select anchor from node where node.n_name = ?),?,?,?)";
	final String insertnoderesolver = "insert into noderesolver values(?,?,?)";
	
	final String deletenode = "delete from node where anchor=?";
	final String deleteedge = "delete from edge where source=? and destination=?";
	final String deletenoderesolver = "delete from noderesolver where nrid=?";
	
	final String updatenode = "update node set anchor=?, x_co=? , y_co=?, nodetype=? where anchor=?" ;
	final String updateedge = "update edge set source=?, destination=?, distance=? , radius=? where source=? and destination=?";
	final String updatenoderesolver = "update noderesolver set nrid=?, anchor=? , label=? where nrid=?";
	
}
