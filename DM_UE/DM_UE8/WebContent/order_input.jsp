<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Bestellung erstellen</title>
</head>
<body>
	<h1>Bestellung erstellen</h1>
	<form action="order_insert.jsp" method="post">
		<table border="0">
			<tr>
				<td><label for="bestellNr">Bestellnummer:</label></td>
				<td><input type="text" name="bestellNr" id="bestellNr"></td>
			</tr>
			<tr>
				<td><label for="datum">Datum:</label></td>
				<td><input type="date" name="datum" id="datum"></td>

			</tr>
		</table>
		<button type="submit">Einfügen</button>
	</form>
	<a href="index.html">Zur Startseite</a>
</body>
</html>