package com.green.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.green.beans.dbpoolbroker.DbConnectionBroker;
import com.green.util.Utility;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection conn = null;
    
	private DbConnectionBroker dbBroker; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		dbBroker = Utility.initBroker(dbBroker);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dbBroker = Utility.initBroker(dbBroker);
		String tokenId=null;
		String userEmail = request.getParameter("email");
		String userPass = request.getParameter("pass");
		String android_id =request.getParameter("androidid");
		String source = request.getParameter("source");	//checks if incoming request is from android or jsp page
		
		/*System.out.println("request email="+userEmail+" pass is "+userPass+" androidid ="+android_id);*/
		//NULL check
		PrintWriter responseWriter = response.getWriter(); 
		
		try{
		if(userEmail == null || userPass == null || android_id ==null)
		{
			if(source != null && source.equals("panel"))
				response.sendRedirect("PanelLogin.jsp?invalid=true");
			else
			Utility.writeError(responseWriter, response, 792);
			responseWriter.flush();
		}
		else
		{
			/*conn=Utility.initConnection();*/
			conn = dbBroker.getConnection();
			if(conn == null)
			{
				Utility.writeError(responseWriter, response, 792);
				return;
			}
			tokenId = queryAndGetdata(conn, userEmail, userPass, android_id);
			if(tokenId == null)
			{
				if(source != null && source.equals("panel"))
					response.sendRedirect("PanelLogin.jsp?invalid=true");
				else
				Utility.writeError(responseWriter, response, 792);
			}
			else
			{
				/*System.out.println("tokenid sending in else is "+tokenId);*/
				//checks if incoming request is from android or jsp page
				if(source != null && source.equals("panel"))
					submitPostData(responseWriter, tokenId, android_id,response);
				else
				{
					/*System.out.println("tokenid sending is "+tokenId);*/
					responseWriter.write(tokenId);
				}
			}

			
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
		responseWriter.flush();
		if(conn != null)
		dbBroker.freeConnection(conn);
		}
		
	
	}

	
	/**queries the db for emailid &password
	 * @return returns the tokenid (to be used for calling web services) if credentials exists
	 * else returns null*/
	  public static String queryAndGetdata(Connection con , String userEmail ,String password ,String androidId)
	  {
		  try {
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String selectQuery = "SELECT userid FROM anktjai_gogreen.`userinfo` WHERE emailid=? and password=?";
				    stmt = con.prepareStatement(selectQuery);
				    stmt.setString(1, userEmail);
				    stmt.setString(2, password);
				 // Create a result set containing userid
				    ResultSet rs = stmt.executeQuery();
				    /*System.out.println("before select query executed");*/
			    if (rs.next()) {
				    //if it exists then update android-id with provide android-id & reply with a tokenid
				      /*System.out.println("username & password exists");*/
				      int userid = rs.getInt("userid");
				      String tokenid =updateAndroidId(con, androidId, userid);
				      if(tokenid != null)
				    	  return tokenid;      
			    }
			    return null;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
/*			finally
			{
				try {
					if(conn!=null)
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
	  }
	  
		 /**inserts android-id in the token_map table
		  * @return if update is succesful it returns a tokenid
		  * else null*/
	  public static String addAndroidId(Connection con ,String android_id ,String tokenid ,int userid)
	  {
		  java.sql.PreparedStatement stmt = null;
		  String insertQuery = "INSERT INTO anktjai_gogreen.token_map VALUES(?,?,?)";
		  /*System.out.println("adding android-id");*/
		    try {
				stmt = con.prepareStatement(insertQuery);
				stmt.setString(1, android_id);
				stmt.setString(2, tokenid);
				stmt.setInt(3, userid);
				
				int rowCount= stmt.executeUpdate();
				if(rowCount >0)
					return tokenid;
				return null;
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
				Utility.deleteAndroidId(conn , android_id);
				return addAndroidId(con, android_id, tokenid, userid);
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	  }

	  
		 /**updates android-id in the token_map table
		  * @return if update is succesful it returns a tokenid
		  * else null*/
	  public static String updateAndroidId(Connection con ,String android_id ,int userid)
	  {
		  java.sql.PreparedStatement stmt = null;
		  String insertQuery = "UPDATE anktjai_gogreen.token_map SET tokenid=?,androidid=? WHERE userid=?";
		  String tokenid = Utility.generateToken();
		    try {
		    	
				stmt = con.prepareStatement(insertQuery);
				stmt.setString(1, tokenid);
				stmt.setString(2, android_id);
				stmt.setInt(3, userid);
				
				int rowCount= stmt.executeUpdate();
				if(!(rowCount > 0))
				{
					// if it failed to update insert the data
					return addAndroidId(con, android_id, tokenid, userid);
				}
				return tokenid;
			}
		    catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
				Utility.deleteAndroidId(conn , android_id);
				return addAndroidId(con, android_id, tokenid, userid);
		    }
		    catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
	  }
	  
	  public void postData(String url, HttpServletResponse response,String auth_token,String androidid)
	  {
		   try {
			HttpClient httpClient = new HttpClient();
			    PostMethod postMethod = new PostMethod(url);
			    NameValuePair[] postDataArray = { new NameValuePair("auth_token", auth_token),
			        new NameValuePair("androidid", androidid)};

			    postMethod.addParameters(postDataArray);
			    httpClient.executeMethod(postMethod);
			    //display the response to the POST method
			    response.setContentType("text/html");
			    java.io.PrintWriter out = response.getWriter();
			    //A "200 OK" HTTP Status Code
			    if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
			      out.println(postMethod.getResponseBodyAsString());
			    } else {
			      out.println("The POST action raised an error: "
			          + postMethod.getStatusLine());
			    }
			    //release the connection used by the method
			    postMethod.releaseConnection();
		}  catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  /***forwards all req & res to Forwarder*/
	  public void callForwarder(HttpServletRequest request, HttpServletResponse response)
	  {
		  RequestDispatcher rd = getServletContext().getRequestDispatcher("/Forwarder");
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

	  }
	  
	 /** *//**submits data to jsp
	 * @param response */
	  public void submitPostData(PrintWriter respWriter,String auth_token,String androidid, HttpServletResponse response)
	  {
		  response.setContentType("text/html");
		  respWriter.write("<img src='images/progress.gif' border='0' alt='Loading, please wait...' />"+
				  "<form name=myform action=PanelHome.jsp method=post>" +
		  "<input type='hidden' name='auth_token' value='"+auth_token+"'>"+
		  "<input type='hidden' name='androidid' value='"+androidid+"'>"+
"</form><script type=text/javascript>document.myform.submit();</script>");
/*		  respWriter.write("<html>" +
		  		"<script language=\"text/javascript\">"+
		  		"document.frmParam.submit();"+
		  		"</script> " +
		  		"<form id= \"form1\" name=\"frmParam\" action=\"/PanelHome.jsp\" method=\"post\">"+
		  		"<input type=\"hidden\" name=\"auth_token\" value=\""+auth_token+"\">"+
		  		"<input type=\"hidden\" name=\"androidid\" value=\""+androidid+"\">"+
		  		"</form>" +
		  		"</html>");*/
		  respWriter.close();
	  }
	  
/*	  public void callPanel()
	  {
		  PostMethod post = new PostMethod("/PanelHome.jsp");
		  HttpClient httpclient = new HttpClient();
		  try {
			int result = httpclient.executeMethod(post);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	  }*/
	  
	 @Override
	public void destroy() {
		 dbBroker.destroy();
		super.destroy();
	}
	  

}
