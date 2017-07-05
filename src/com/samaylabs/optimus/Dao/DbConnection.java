package com.samaylabs.optimus.Dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Tulve Shabab Kasim
 *
 */
public class DbConnection implements DbConstants{

	Connection connection = null;
	Statement statement = null;
	
	private String Url = "jdbc:mysql://" + HOST + "/" + DBNAME;
	
	public DbConnection(){}
	
	
	/**
	 * 
	 * @param dbname name of database
	 * @param username database username name
	 * @param password database password
	 * @return Connection object for database connection
	 */
	public Connection getConncetion(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(Url,USERNAME,PASSWORD);
		}catch(Exception e){
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 
	 * @return Statement object for database query
	 */
	public Statement getStatement(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(Url,USERNAME,PASSWORD);
			statement = connection.createStatement();
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return statement;
		
	}

}
