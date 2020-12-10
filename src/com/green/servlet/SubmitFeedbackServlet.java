package com.green.servlet;

/**This servlet gets as parameter
 * auth_token
	android-id
	E-mail id(may be different from e-mail id in user information table)
	Feedback_type
	Feedback_text
	
	*This servlet sends returns response code OK if submitted
	*else returns response code	790 OR UnAuthorized Access**/
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;

import com.green.beans.dbpoolbroker.DbConnectionBroker;
import com.green.util.Utility;

/**
 * Servlet implementation class SubmitFeedbackServlet
 */
public class SubmitFeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection conn= null;   

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		conn =dbBroker.getConnection();
		PrintWriter responseWriter = response.getWriter(); 

		String androidId = request.getParameter("android-id");
		String auth_token = request.getParameter("auth_token");
		String responseEmailId = request.getParameter("email-id");
		String feedbackType = request.getParameter("feedback_type");
		String feedbackText = request.getParameter("feedback_text");
		try{
		//NULL check
		if(androidId == null || auth_token == null || feedbackText == null)
		{
			Utility.writeError(responseWriter, response,792);
		}
		else
		{
			int userId = Utility.verifyAuthentication(conn ,androidId, auth_token);
			if(userId != -9)
			{
				
				if(insertFeedback(conn, userId, responseEmailId, feedbackType, feedbackText))
					response.setStatus(HttpStatus.SC_OK);
				else
					Utility.writeError(responseWriter, response,792);
			}
			else
			{
				Utility.writeError(responseWriter, response,790);
			}
			
/*		    try {
				conn.close();
			    System.out.println("Disconnected from database");
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
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
	
	/**inserts feedback to  the db*/
	  public static boolean insertFeedback(Connection con , int userId ,String responseEmail ,
			  String feedbackType ,String feedbackText)
	  {
		  String insertQuery="INSERT INTO feedback(userid,responseemailid,feedbacktype,feedbacktext) VALUES(?,?,?,?)";
		  java.sql.PreparedStatement stmt = null;
		  
		    try 
		    {
				stmt = con.prepareStatement(insertQuery);
				stmt. setInt(1, userId);
				stmt.setString(2, responseEmail);
				stmt.setString(3, feedbackType);
				stmt.setString(4, feedbackText);
				
				int rowCount= stmt.executeUpdate();
				/*System.out.println("rowCount  on insert is "+rowCount);*/
				if(rowCount > 0)
				{
					// if it failed to update insert the data
					/*System.out.println("returning true on insert");*/
					return true;
				}
				/*System.out.println("returning false on insert");*/
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
	  
	  }

}
