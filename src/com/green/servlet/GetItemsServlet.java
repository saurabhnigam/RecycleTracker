package com.green.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.green.beans.QueryResultBean;
import com.green.beans.dbpoolbroker.DbConnectionBroker;
import com.green.util.Utility;

/**
 * Servlet implementation class GetItemsServlet
 */

public class GetItemsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
	
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
		PrintWriter responseWriter = null;
		try {
		conn = dbBroker.getConnection();
		responseWriter = response.getWriter(); 

		String androidId = request.getParameter("androidid");
		String auth_token = request.getParameter("auth_token");
		/*System.out.println("received androidid is "+androidId+" auth-token is "+auth_token);*/
		//NULL check
		if(androidId == null || auth_token == null)
		{
			Utility.writeError(responseWriter, response , 792);
		}
		else
		{
		int userId = Utility.verifyAuthentication(conn ,androidId, auth_token);
		if(userId != -9)
		{
			String xmlResponse =Utility.generateItemXML("root","item" ,queryAndGetdata(conn ,userId));
			response.setContentType("text/xml");
			responseWriter.write(xmlResponse != null ?xmlResponse :"<error>No data.</error>");
			/*responseWriter.write(writeTestData());*/
		}
		else
		{
			Utility.writeError(responseWriter, response,790);
		}
		
/*
			conn.close();*/
		    /*System.out.println("Disconnected from database");*/
		
		}
	    
	    }catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
		responseWriter.flush();
		dbBroker.freeConnection(conn);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	
	
	/**queries the db*/
	  public static Vector<QueryResultBean> queryAndGetdata(Connection con , int userId)
	  {
		  try {
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String selectQuery = "SELECT * FROM task_table WHERE userid = ?";
				    stmt = con.prepareStatement(selectQuery);
				    stmt.setInt(1, userId);
				 // Create a result set containing all data from feedback table
				    ResultSet rs = stmt.executeQuery();
			      
			    /*System.out.println("Got resultset of userid "+userId);*/
			    Vector<QueryResultBean> queryResult = new Vector<QueryResultBean>();
			    // Fetch each row from the result set
			    while (rs.next()) {
				      /*System.out.println("wwhile parsing rs");*/
			        // Get the data from the row using the column index
			        /*s = rs.getString(1);*/

			        // Get the data from the row using the column name
			        queryResult.add(new QueryResultBean(rs.getString("itemName"),
			        		rs.getString("itemquantity"), 
			        		rs.getString("latitude"), 
			        		rs.getString("longitude"),
			        		rs.getString("task"),
			        		rs.getInt("taskid"), 
			        		rs.getInt("userid") 
			        		));
			    }
			    /*System.out.println("returning  vector");*/
			    return queryResult;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
	  }
	  
	  public static Connection getConnection() 
      throws SQLException{
		  return DriverManager.getConnection("jdbc:jdc:jdcpool");
}
	  
/*	  public String writeXMLResult()
	  {
		  return ""+
"<message>valid</message>";
	  }*/
}
