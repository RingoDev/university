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
<title>Lieferung erstellen</title>
</head>
<body>
	<h1>Lieferung erstellen</h1>

	<form action="delivery_insert.jsp" method="post">
		<table style="border: 0">
			<tr>
				<td><label for="lieferNr">LieferNr:</label></td>
				<td><input type="text" name="lieferNr" id="lieferNr"></td>
			</tr>
			<tr>
				<td><label for="lieferZeitpunkt">lieferZeitpunkt:</label></td>
				<td><input type="datetime-local" name="lieferZeitpunkt" id="lieferZeitpunkt"></td>
			</tr>
		</table>
		<button type="submit">Einfügen</button>
	</form>
	
	<a href="index.html">Zur Startseite</a>
</body>
</html>