<%@page import="com.green.util.Utility"%>
<html>
<head>

<link rel="stylesheet" type="text/css" media="screen" href="screen.css" />


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login to GoGreen</title>
<br>GoGreenApp Version1.4<br>
</head>
<body>

<img src="images/gogreen.jpeg">
<div align=center>Admin Login</div>
<form method="post" action="LoginServlet" name="loginform"><br>
Username:&nbsp; <input name="email"> <br>
Password:&nbsp;<input name="pass" type="password"> <br>
<input name="androidid" type="hidden" value="<%=Utility.generateToken()%>">
<input name="source" type="hidden" value="panel">
<button name="Submit" value="Submit"> Submit </button>
</form>
</body>
</html>

