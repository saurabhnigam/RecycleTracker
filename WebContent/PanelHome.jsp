<html>
<head>
<link rel="stylesheet" type="text/css" media="screen" href="screen.css" />
<script type="text/javascript">
function showRecycles()
{
var xmlhttp;
var txt,x,xx,i;

if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    
    txt="<table border='1'><tr>Recycles</tr><tr><th>TaskId</th><th>User</th><th>Email</th><th>Item</th><th>Quantity</th><th>Latitude</th><th>Longitude</th></tr>";
    x=xmlhttp.responseXML.documentElement.getElementsByTagName("tasks");
    for (i=0;i<x.length;i++)
      {
      txt=txt + "<tr>";
      xx=x[i].getElementsByTagName("taskid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
      xx=x[i].getElementsByTagName("userid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("emailid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
       xx=x[i].getElementsByTagName("itemName");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("itemQuantity");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("lat");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("lon");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }

        
        
      txt=txt + "</tr>";
      }
    txt=txt + "</table>";
    document.getElementById("txtRecycle").innerHTML=txt;
    }
  else if (xmlhttp.readyState==4 && xmlhttp.status==790)
  {
	  window.location='PanelLogin.jsp';

  }
    }
  
xmlhttp.open("GET",'GetInfoServlet?auth_token=<%=request.getParameter("auth_token")%>&androidid=<%=request.getParameter("androidid")%>&list=recycle',true);
xmlhttp.send();

}

function showConsumption()
{
var xmlhttp;
var txt,x,xx,i;

if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    
    txt="<table border='1'><tr>Consumption</tr><tr><th>TaskId</th><th>User</th><th>Email</th><th>Item</th><th>Quantity</th><th>Latitude</th><th>Longitude</th></tr>";
    x=xmlhttp.responseXML.documentElement.getElementsByTagName("tasks");
    for (i=0;i<x.length;i++)
      {
      txt=txt + "<tr>";
      xx=x[i].getElementsByTagName("taskid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
      xx=x[i].getElementsByTagName("userid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("emailid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
       xx=x[i].getElementsByTagName("itemName");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("itemQuantity");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("lat");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("lon");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }

        
        
      txt=txt + "</tr>";
      }
    txt=txt + "</table>";
    document.getElementById('txtConsumption').innerHTML=txt;
    }
  else if (xmlhttp.readyState==4 && xmlhttp.status==790)
  {
	  window.location='PanelLogin.jsp';

  }
    }
  
xmlhttp.open("GET",'GetInfoServlet?auth_token=<%=request.getParameter("auth_token")%>&androidid=<%=request.getParameter("androidid")%>&list=consumption',true);
xmlhttp.send();

}

function showFeedback()
{
var xmlhttp;
var txt,x,xx,i;

if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    
    txt="<table border='1'><tr>Feedbacks</tr><tr><th>Feedback Id</th><th>Email Id</th><th>Response Email</th><th>Feedback type</th><th>Feedback</th></tr>";
    x=xmlhttp.responseXML.documentElement.getElementsByTagName("feedback");
    for (i=0;i<x.length;i++)
      {
      txt=txt + "<tr>";
      xx=x[i].getElementsByTagName("feedbackid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
      xx=x[i].getElementsByTagName("emailid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("responseemailid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
       xx=x[i].getElementsByTagName("feedbacktype");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
        xx=x[i].getElementsByTagName("feedbacktext");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
 
      txt=txt + "</tr>";
      }
    txt=txt + "</table>";
    document.getElementById('txtFeedback').innerHTML=txt;
    }
  else if (xmlhttp.readyState==4 && xmlhttp.status==790)
  {
	  window.location='PanelLogin.jsp';

  }
    }
  
xmlhttp.open("GET",'GetInfoServlet?auth_token=<%=request.getParameter("auth_token")%>&androidid=<%=request.getParameter("androidid")%>&list=feedback',true);
xmlhttp.send();

}

function showUsers()
{
var xmlhttp;
var txt,x,xx,i;
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    
    txt="<table border='1'><tr>Users</tr><tr><th>User Id</th><th>Email Id</th></tr>";
    x=xmlhttp.responseXML.documentElement.getElementsByTagName("users");
    for (i=0;i<x.length;i++)
      {
      txt=txt + "<tr>";
      xx=x[i].getElementsByTagName("userid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }
      xx=x[i].getElementsByTagName("emailid");
        {
        try
          {
          txt=txt + "<td>" + xx[0].firstChild.nodeValue + "</td>";
          }
        catch (er)
          {
          txt=txt + "<td> </td>";
          }
        }

        
      txt=txt + "</tr>";
      }
    txt=txt + "</table>";
    document.getElementById('txtUser').innerHTML=txt;
    }
  else if (xmlhttp.readyState==4 && xmlhttp.status==790)
  {
	  window.location='PanelLogin.jsp';

  }
    }
  
xmlhttp.open("GET",'GetInfoServlet?auth_token=<%=request.getParameter("auth_token")%>&androidid=<%=request.getParameter("androidid")%>&list=userinfo',true);
xmlhttp.send();

}

function logout()
{
var xmlhttp;
var txt,x,xx,i;
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
	  window.location='PanelLogin.jsp';

    }

    }
  
xmlhttp.open("GET",'GetInfoServlet?auth_token=<%=request.getParameter("auth_token")%>&androidid=<%=request.getParameter("androidid")%>&list=userinfo',true);
xmlhttp.send();

}
<%
String token =request.getParameter("auth_token");
String androidid=request.getParameter("androidid");

if((token == null) || (token.equals("")) || (androidid == null) || (androidid.equals("")))
{
	response.sendRedirect("/PanelLogin.jsp");
}
%>
</script>
<body>


<button onclick="showRecycles()">Show Recycles</button>
<button onclick="showConsumption()">Show Consumption</button>
<button onclick="showUsers()">Show Users</button>
<button onclick="showFeedback()">Show Feedbacks</button>
<button onclick="logout()">Logout</button>
<p><span id="txtRecycle"></span></p> 

<p><span id="txtFeedback"></span></p> 
<p><span id="txtConsumption"></span></p> 

<p><span id="txtUser"></span></p> 

<p><span id="txtHint"></span></p> 
</body>
<br>GoGreenApp Version1.4<br>
</html>


