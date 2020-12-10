package com.green.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import com.green.beans.QueryResultBean;
import com.green.beans.dbpoolbroker.DbConnectionBroker;
import com.green.pool.JDCConnectionDriver;


public class Utility {

	/*	    String url = "jdbc:mysql://localhost:3306/";*/

	/*	    String dbName = "gogreen2";
		    String driver = "com.mysql.jdbc.Driver";
		    String userName = "root"; 
		    String password = "admin";*/


		    public static String dbName = "anktjai_gogreen";
/*		    private static String url = "jdbc:mysql://jainankit.com:3306/"+dbName;*/
			private static String url = "jdbc:mysql://localhost:3306/"+dbName;
		    private static String driver = "com.mysql.jdbc.Driver";
		    private static String userName = "anktjai_user"; 
		    private static String password = "918824QRTC";
/*		    private static String userName = "anktjai_root"; 
		    private static String password = "QRTC787878IO";*/
		    
		    private static int minConnection = 10;
		    private static int maxConnection = 50;
			private static String logFileName="/home/anktjai/gogreen/GoGreenDB.log";
			/**Time in days between connection resets. (Reset does a basic cleanup)**/
			private static double maxConnTime=5;

	//Static class instantiation
	  static {
		  //initiate the class to later use getConnection
	        try{

		    	new JDCConnectionDriver(
		    			driver, 
		    			url+dbName,
		    			userName, password);
	        }catch(Exception e){}
	  }

	

	/**generate xml test 
	 * @param root specifies name of root element
	 * @param element specifies name of child element
	 * @param vector */
	public static String generateItemXML(String root ,String element, Vector<QueryResultBean> vector)
	{
		try {
			int count = vector != null ? vector.size() : 0;
			if(count == 0)
				return null;
			
			String xmlDoc;
			/*root = "root";*/
/*			DocumentBuilderFactory documentBuilderFactory = 
			                               DocumentBuilderFactory.newInstance();
			    DocumentBuilder documentBuilder = 
			                          documentBuilderFactory.newDocumentBuilder();
			    Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement(root);
			    document.appendChild(rootElement);*/
			    Iterator<QueryResultBean> iterator = vector.iterator();
			    QueryResultBean tempQueryBean = null;
			xmlDoc="<gogreen>";
					
			
			    while(iterator.hasNext())
			    {
			    	xmlDoc+="<item>";
			    	tempQueryBean = iterator.next();
			    	/*					Element em = document.createElement(element);*/
					xmlDoc+="<taskid>"+tempQueryBean.getTaskid()+"</taskid>";
					xmlDoc+="<userid>"+tempQueryBean.getUserid()+"</userid>";
					xmlDoc+="<itemName>"+ tempQueryBean.getItemName()+"</itemName>";
					xmlDoc+="<itemQuantity>"+ tempQueryBean.getQuantity()+"</itemQuantity>";
					xmlDoc+="<lat>" +tempQueryBean.getLatitude()+"</lat>";
					xmlDoc+="<lon>"+ tempQueryBean.getLongitude()+"</lon>";
					xmlDoc+="<task>"+ tempQueryBean.getTask()+"</task>";
					
					xmlDoc+="</item>";
/*					rootElement.appendChild(em);*/
			    }
			    xmlDoc+="</gogreen>"; 
			/*for (int i = 1; i <= count; i++){
			  element = "element";
			  String data = "data";
			  Element em = document.createElement(element);
			  em.appendChild(document.createTextNode(data));
			  em.setAttribute("item_id", );
			  rootElement.appendChild(em);
			}*/
/*			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    Transformer transformer = transformerFactory.newTransformer();
			    DOMSource source = new DOMSource(document);
			    StringWriter sw = new StringWriter();
			    StreamResult result =  new StreamResult(sw);
			    transformer.transform(source, result);
			    String xmlString = sw.toString();*/
/*			    /*System.out.println("Here's the xml:\n\n" + xmlString);*/
			    
			    return xmlDoc;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		} 
	  }
	
	/***generates unique token*/
	public static String generateToken()
	{
	    UUID randomUUID = UUID.randomUUID();
	    String tokenId = randomUUID.toString()+System.currentTimeMillis();
	    /*System.out.println("tokenid generated is "+tokenId);*/
	    return tokenId;
	}
	
/*	*//**Initiates connection to DB**//*
	public static java.sql.Connection initConnection()
	{

	    
        try {
            Class.forName(driver).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
	    try {
	      return getConnection() ;

	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }

		
	}*/
	
	
	  public static java.sql.Connection getConnection() 
      throws SQLException{
		  return DriverManager.getConnection("jdbc:jdc:jdcpool");
}
	  
	  /**returns userId on the basis of adroidId & auth_token if exists else null
	 * @param conn 
	 * @returns -9 if no user exists by this credentials**/
	  public static int verifyAuthentication(Connection con, String androidId ,String auth_token)
	  {

		  try {
			  //using preparedstatements for security against SQL injection
				  java.sql.PreparedStatement stmt = null;
				  String selectQuery = "SELECT userid FROM token_map WHERE androidid = ? && tokenid=?";
				    stmt = con.prepareStatement(selectQuery);
				    stmt.setString(1, androidId);
				    stmt.setString(2, auth_token);
				 // Create a result set containing userid
				    ResultSet rs = stmt.executeQuery();
			      
			    /*System.out.println("Got resultset");*/

			    if (rs.next()) {
				    //it exists 
				      /*System.out.println("androidId & token exists");*/
				      int userid = rs.getInt("userid");
				      return userid;
			    }
			    return -9;

			} catch (SQLException e) {
				e.printStackTrace();
				return -9;
			}

	  
	  }
	  
	  /**writes error to response*/
	  public static void writeError(PrintWriter responseWriter ,HttpServletResponse  response, int responsecode)
	  {
			response.setStatus(responsecode);
			responseWriter.write("Invalid data");
			responseWriter.flush();
	  }
	  
	  /**deletes androidId if it exista with other account***/
		public static boolean deleteAndroidId(Connection con , String androidid) {

			  try {
				  //using preparedstatements for security against SQL injection
					  java.sql.PreparedStatement stmt = null;
					  String deleteQuery = "delete from token_map where androidid=?";
					    stmt = con.prepareStatement(deleteQuery);
					    stmt.setString(1, androidid);
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
	  
		
		/**checks whether emailid or androidid exists
		 * 
		 * @return if emailid exists returns false
		 * if androidid exists it dletes it*/
		  public static boolean checkIfValuesExist(Connection con , String userEmail ,String androidId)
		  {
			  try {
				  //using preparedstatements for security against SQL injection
					  java.sql.PreparedStatement stmt1 = null;
					  String selectEmailIdQuery = "SELECT emailid FROM userinfo WHERE emailid=?";
					    stmt1 = con.prepareStatement(selectEmailIdQuery);
					    stmt1.setString(1, userEmail);

					    ResultSet rs = stmt1.executeQuery();

				    if (rs.next()) {
					    //if it exists then update android-id with provide android-id & reply with a tokenid
					      /*System.out.println("emailid exists");*/					      return false;      
				    }
				    
//now checking androidid
				    
					  java.sql.PreparedStatement stmt2 = null;
					  String selectQuery = "SELECT androidid FROM token_map WHERE androidid=?";
					    stmt2 = con.prepareStatement(selectQuery);
					    stmt2.setString(1, androidId);

					    ResultSet rs2 = stmt2.executeQuery();

				    if (rs2.next()) {
					    //if it exists then update android-id with provide android-id & reply with a tokenid
					      return deleteAndroidId(con, androidId);   
				    }
				    
				    return true;
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
/*				finally
				{
					try {
						if(con!=null)
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
						return false;
					}
				}*/

		  }

		  
		  /**Initializes broker with db username & password**/
		  public static  DbConnectionBroker initBroker(DbConnectionBroker dbBroker) 
		  {
		        // The below statement sets up a Broker with a minimun pool size of minConnections
		        // and a maximum of maxConnections.  The log file will be created in 
		        // GogreenDb.log and the pool connections will be
		        // restarted once in 2 days.
		        try {
		        	dbBroker = new DbConnectionBroker(driver,
		                                         url,
		                                         userName,
		                                         password,
		                                         minConnection,
		                                         maxConnection,
		                                         logFileName,
		                                         maxConnTime);
		        return dbBroker;
		        }
		        catch (IOException e5)  {
		        	e5.printStackTrace();
		        	return null;
		        }
			  

		  }
	  
/*	public static String generateUniqueToken()
	{
	      UUID g = UUID.NewGuid();
	      string s = g.ToString();

	      DateTime before = DateTime.Now;
	      for (int i = 0; i < 10000; i++)
	      {
	        bool retVal = IsGuid(s);
	      }
	      Console.WriteLine(DateTime.Now.Subtract(before));

	      before = DateTime.Now;
	      for (int i = 0; i < 10000; i++)
	      {
	        bool retVal = IsGuid2(s);
	      }
	      Console.WriteLine(DateTime.Now.Subtract(before));
	      Console.ReadLine();

	}*/

	
	
	/*	generates XML
	  @return xml in string
	public static String DomXmlGeneration() {
      try {
          /////////////////////////////
          //Creating an empty XML Document

          //We need a Document
          DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
          Document doc = docBuilder.newDocument();

          ////////////////////////
          //Creating the XML tree

          //create the root element and add it to the document
          Element root = doc.createElement("root");
          doc.appendChild(root);

          //create a comment and put it in the root element
          Comment comment = doc.createComment("Just a thought");
          root.appendChild(comment);

          //create child element, add an attribute, and add to root
          Element child = doc.createElement("child");
          child.setAttribute("name", "value");
          root.appendChild(child);

          //add a text element to the child
          Text text = doc.createTextNode("Filler, ... I could have had a foo!");
          child.appendChild(text);

          /////////////////
          //Output the XML

          //set up a transformer
          TransformerFactory transfac = TransformerFactory.newInstance();
          Transformer trans = transfac.newTransformer();
          trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
          trans.setOutputProperty(OutputKeys.INDENT, "yes");

          //create string from xml tree
          StringWriter sw = new StringWriter();
          StreamResult result = new StreamResult(sw);
          DOMSource source = new DOMSource(doc);
          trans.transform(source, result);
          String xmlString = sw.toString();

          //print xml
          /*System.out.println("Here's the xml:\n\n" + xmlString);
          return xmlString;
      } catch (Exception e) {
          e.printStackTrace();
  		return null;
      }

  }*/
}
