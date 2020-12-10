package com.green.panelServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class GetInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection conn = null;
    private PrintWriter log;
	private DbConnectionBroker dbBroker; 

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		dbBroker = Utility.initBroker(dbBroker);
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		conn = dbBroker.getConnection();
		
		PrintWriter responseWriter = response.getWriter(); 

		String androidId = request.getParameter("androidid");
		String auth_token = request.getParameter("auth_token");
		//gets reuested list
		String list = request.getParameter("list");
		/*/*System.out.println("received androidid is "+androidId+" auth-token is "+auth_token);*/
		try{
		//NULL check
		if(androidId == null || auth_token == null ||list == null)
		{
			Utility.writeError(responseWriter, response , 792);
		}
		else
		{
		int userId = Utility.verifyAuthentication(conn ,androidId, auth_token);
		//userid==-1 i.e. admin
		if(userId == -1)
		{
			/*System.out.println("userId is "+userId);*/
			String xmlResponse = null;
			
			if(list.equals("recycle"))
				xmlResponse =queryAndGetTaskLists(conn , "r");
			else if(list.equals("consumption"))
				xmlResponse =queryAndGetTaskLists(conn , "c");
			else if(list.equals("feedback"))
				xmlResponse =queryAndGetFeedbacks(conn);
			else if(list.equals("userinfo"))
				xmlResponse =queryAndGetUsers(conn);
			else if(list.equals("logout"))
				deleteToken(conn ,response,androidId , auth_token);
			response.setContentType("text/xml");
			responseWriter.write(xmlResponse != null ?xmlResponse :"No data.");
		}
		else
		{
			Utility.writeError(responseWriter, response,790);
		}
		
		}
		}catch (Exception e) {
			e.printStackTrace();
		}

		responseWriter.flush();
		/*dbBroker.freeConnection(conn);*/

	}

	 @Override
		public void destroy() {
			 dbBroker.destroy();
			super.destroy();
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	
	
	/**queries the to get tasks(Recycle/Consumption) details i.e */
	  public static String queryAndGetTaskLists(Connection con ,  String task)
	  {
		  try {
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String selectQuery = "SELECT taskid,task_table.userid,itemname,itemquantity,latitude,longitude,emailid" +
				  		" FROM task_table inner join userinfo where userinfo.userid=task_table.userid && task_table.task like ?";
				    stmt = con.prepareStatement(selectQuery);
				    stmt.setString(1, task+"%");
				 // Create a result set containing all data from feedback table
				    ResultSet rs = stmt.executeQuery();
			    
			    String xmlDoc="<gogreen>";
			    // Fetch each row from the result set
			    while (rs.next()) {
				      /*System.out.println("wwhile parsing rs");*/
			        xmlDoc +="<tasks>";
						xmlDoc+="<taskid>"+rs.getInt("taskid")+"</taskid>";
						xmlDoc+="<userid>"+rs.getInt("userid")+"</userid>";
						xmlDoc+="<emailid>"+ rs.getString("emailid")+"</emailid>";
						xmlDoc+="<itemName>"+ rs.getString("itemName")+"</itemName>";
						xmlDoc+="<itemQuantity>"+ rs.getString("itemquantity")+"</itemQuantity>";
						xmlDoc+="<lat>" +rs.getString("latitude")+"</lat>";
						xmlDoc+="<lon>"+ rs.getString("longitude")+"</lon>";
					xmlDoc+="</tasks>";
			    }
			    xmlDoc+="</gogreen>";
			    /*System.out.println("xmlDoc is "+xmlDoc);*/
			    return xmlDoc;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
	  }
	  
	  
	  /**queries the to get feedback details */
	  public static String queryAndGetFeedbacks(Connection con)
	  {
		  try {
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String selectQuery = "SELECT feedbackid,emailid,responseemailid,feedbacktype,feedbacktext FROM feedback inner join userinfo where userinfo.userid=feedback.userid";
				    stmt = con.prepareStatement(selectQuery);
				 // Create a result set containing all data from feedback table
				    ResultSet rs = stmt.executeQuery();
			    
			    String xmlDoc="<gogreen>";
			    // Fetch each row from the result set
			    while (rs.next()) {
				      /*System.out.println("wwhile parsing rs");*/
			        xmlDoc +="<feedback>";
						
			        	xmlDoc+="<feedbackid>"+rs.getInt("feedbackid")+"</feedbackid>";
						xmlDoc+="<emailid>"+ rs.getString("emailid")+"</emailid>";
						xmlDoc+="<responseemailid>"+ rs.getString("responseemailid")+"</responseemailid>";
						xmlDoc+="<feedbacktype>"+ rs.getString("feedbacktype")+"</feedbacktype>";
						xmlDoc+="<feedbacktext>" +rs.getString("feedbacktext")+"</feedbacktext>";

					xmlDoc+="</feedback>";
			    }
			    xmlDoc+="</gogreen>";
			    /*System.out.println("xmlDoc is "+xmlDoc);*/
			    return xmlDoc;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
	  }
	  
	  /**queries the to get users details */
	  public static String queryAndGetUsers(Connection con)
	  {
		  try {
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String selectQuery = "select userid,emailid from userinfo";
				    stmt = con.prepareStatement(selectQuery);
				 // Create a result set containing all data from feedback table
				    ResultSet rs = stmt.executeQuery();
			    
			    String xmlDoc="<gogreen>";
			    // Fetch each row from the result set
			    while (rs.next()) {
				      /*System.out.println("wwhile parsing rs");*/
			        xmlDoc +="<users>";
						
			        	xmlDoc+="<userid>"+rs.getInt("userid")+"</userid>";
						xmlDoc+="<emailid>"+ rs.getString("emailid")+"</emailid>";
					xmlDoc+="</users>";
			    }
			    xmlDoc+="</gogreen>";
			    /*System.out.println("xmlDoc is "+xmlDoc);*/
			    return xmlDoc;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
	  }
	  
	  
	  public static Connection getConnection() 
      throws SQLException{
		  return DriverManager.getConnection("jdbc:jdc:jdcpool");
	  }
	  
		/**sets token id to null & redirects to PanelLogin.jsp
		 * @param auth_token 
		 * @param androidId **/
		private boolean deleteToken(Connection con,HttpServletResponse response, String androidId, String auth_token) {
			  try {
				  //using preparedstatements for security against SQL injection
					  java.sql.PreparedStatement stmt = null;
					  String deleteQuery = "delete from token_map where tokenid=? && androidid=?";
					    stmt = con.prepareStatement(deleteQuery);
					    stmt.setString(1, auth_token);
					    stmt.setString(2, androidId);
					    int resultedRows = stmt.executeUpdate();

					    if(resultedRows > 0)
					    return true;

			  }
			  catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}

}
