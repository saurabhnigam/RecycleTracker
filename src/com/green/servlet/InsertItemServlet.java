package com.green.servlet;

/**This servlet gets as parameter
 * auth_token
	android-id
	item_name
	quantity
	Latitude
	Longitude
	Task(Recycle or Consumption)
	
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
public class InsertItemServlet extends HttpServlet {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {


		try {
			conn =dbBroker.getConnection();
			PrintWriter responseWriter = response.getWriter(); 

			String androidId = request.getParameter("android-id");
			String auth_token = request.getParameter("auth_token");
			String item_name = request.getParameter("item_name");
			String quantity = request.getParameter("quantity");
			String latitude = request.getParameter("latitude");
			String longitude = request.getParameter("longitude");
			String task = request.getParameter("task");
			/*System.out.println("androidId="+androidId+" auth_token="+auth_token+" item_name ="+item_name);*/
			//NULL check
			if(androidId == null || auth_token == null || item_name == null || task == null)
			{
				Utility.writeError(responseWriter, response,792);
			}
			else
			{
				int userId = Utility.verifyAuthentication(conn ,androidId, auth_token);
				
				/*System.out.println("userId on insert is "+userId);
				if(userId != -9)
				{
					if(insertItem(conn, userId, item_name, quantity, latitude, longitude ,task))
						response.setStatus(HttpStatus.SC_OK);
					else
						Utility.writeError(responseWriter, response,792);
				}
				else
				{
					Utility.writeError(responseWriter, response,790);
				}
				
/*			    try {
					conn.close();
				    /*System.out.println("Disconnected from database");
				} catch (SQLException e) {
					e.printStackTrace();
				}*/
			}
			

			responseWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
		dbBroker.freeConnection(conn);
		}
	}
	
	/**inserts feedback to  the db
	 * @param task 
	 * @param longitude */
	  public boolean insertItem(Connection con , int userId ,String  item_name,
			  String quantity ,String latitude, String longitude, String task)
	  {
		  String insertQuery="INSERT INTO task_table (userid,itemname,itemquantity," +
		  		"latitude,longitude,task) values(?,?,?,?,?,?)";
		  java.sql.PreparedStatement stmt = null;
		  
		    try 
		    {
				stmt = con.prepareStatement(insertQuery);
				stmt. setInt(1, userId);
				stmt.setString(2, item_name);
				stmt.setInt(3, stringToInt(quantity));
				stmt.setString(4, latitude);
				stmt.setString(5, longitude);
				stmt.setString(6, task);
				
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
		    
		    /***converts string to int
		     * By default returns 1*/
		    private int stringToInt(String qtyString)
		    { 
		    	int qtyInt = 1;
		    
		    	if(qtyString != null)
		    	{
		    		try{
		    		qtyInt = Integer.parseInt(qtyString);
		    		return qtyInt;
		    		}
		    		catch (Exception e) {
						return 1;
					}
		    	}
		    	else
		    		return 1;
		    }

}
