package com.green.servlet;
/***This servlet registers login credentials 
 * Request params are 
 * 	email 
	Pass
	android-id**/
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.green.beans.dbpoolbroker.DbConnectionBroker;
import com.green.util.Utility;

/**
 * Servlet implementation class GetItemsServlet
 */

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection conn = null;
	
	private DbConnectionBroker dbBroker; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		dbBroker = Utility.initBroker(dbBroker);
	}
	 @Override
		public void destroy() {
			 dbBroker.destroy();
			super.destroy();
		}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userEmail = request.getParameter("email");
		String userPass = request.getParameter("pass");
		String android_id =request.getParameter("android-id");
		
		PrintWriter responseWriter = response.getWriter(); 
		try{
		if(userEmail == null || userPass == null || android_id ==null)
		{
			Utility.writeError(responseWriter, response, 792);
			responseWriter.flush();
			/*System.out.println("null values");
			System.out.println("userEmail is "+userEmail+" pass is "+userPass+" android-id is "+android_id);*/
		}
		else
		{
			conn=dbBroker.getConnection();
			writeToDB(conn, userEmail, userPass, android_id);
		}
/*		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
		dbBroker.freeConnection(conn);
		}
	}

	

	
	/**queries the db*/
	  public static boolean writeToDB(Connection con , String emailId, String password ,String android_id)
	  {
		  try {
			  //TODO check if userid exists previously
			  if(!Utility.checkIfValuesExist(con, emailId, android_id))
				  return false;
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String insertQuery = "INSERT INTO userinfo(EMAILID,PASSWORD) VALUES(?,?)";
				  
				    stmt = con.prepareStatement(insertQuery);
				    stmt.setString(1, emailId);
				    stmt.setString(2, password);
				 // Create a result set containing all data from feedback table
				    int  rowCount= stmt.executeUpdate();
				    ResultSet rs =stmt.getGeneratedKeys();
				    /*System.out.println("getting generated keys");*/
				    int userid = 0 ;
				    if ( rs.next() ) {
				    	userid=rs.getInt(1);
				    }
				    /*System.out.println("user id added is "+userid);*/
				    return addAndroidId(conn ,android_id , userid);
				    


			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
				Utility.deleteAndroidId(conn , android_id);
				return writeToDB(con, emailId, password, android_id);
			}
		  
		  catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
	  }
	  

	  
	 /**adds android-id to token_map table*/
	  public static boolean addAndroidId(Connection con ,String android_id ,int userid)
	  {
		  java.sql.PreparedStatement stmt = null;
		  String insertQuery = "INSERT INTO token_map(androidid ,userid , tokenid) VALUES(?,?,?)";
		  
		    try {
				stmt = con.prepareStatement(insertQuery);
				stmt.setString(1, android_id);
				stmt.setInt(2, userid);
				stmt.setInt(3, -1);
				
				int rowCount= stmt.executeUpdate();
				if(rowCount >0)
					return true;
				return false;
			} catch (Throwable e) {
				e.printStackTrace();
				return false;
			} 
	  }
}
