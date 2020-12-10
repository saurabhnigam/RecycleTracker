package com.green.servlet;

/**This servlet gets as parameter
 * auth_token
	android-id
	
	*This servlet returns response as total rewards
	*else returns response code	790 OR UnAuthorized Access**/
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

public class RewardsServlet extends HttpServlet {
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
			String rewards =String.valueOf(queryAndGetRewards(conn ,userId));
			responseWriter.write(rewards != null ?rewards :"<error>No data.</error>");
			/*responseWriter.write(writeTestData());*/
		}
		else
		{
			Utility.writeError(responseWriter, response,790);
		}
		
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


	
	
	/**queries the db for rewards*/
	  public static long queryAndGetRewards(Connection con , int userId)
	  {
		  try {
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String selectQuery = "SELECT SUM(itemquantity) as totalquantity FROM task_table WHERE userid = ?";
				    stmt = con.prepareStatement(selectQuery);
				    stmt.setInt(1, userId);
				    ResultSet rs = stmt.executeQuery();
			      
			    if (rs.next()) {
				      int rewards = rs.getInt("totalquantity");
				      return rewards;
			    }

			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
		return -1;
	  }

}
