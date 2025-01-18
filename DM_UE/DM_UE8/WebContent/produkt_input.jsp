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
<title>Produkt erstellen</title>
</head>
<body>
	<h1>Produkt erstellen</h1>
	<form action="produkt_insert.jsp" method="post">
		<table style="border: 0;">
			<tr>
				<td><label for="produktNr">Produktnummer:</label></td>
				<td><input type="number" name="produktNr" id="produktNr"></td>
			</tr>
			<tr>
				<td><label for="gtin">GTIN:</label></td>
				<td><input type="text" name="gtin" id="gtin"></td>
			</tr>
			<tr>
				<td><label for="bezeichnung">Bezeichnung:</label></td>
				<td><input type="text" name="bezeichnung" id="bezeichnung"></td>
			</tr>
			<tr>
				<td><label for="hkPreis">Herstellkosten:</label></td>
				<td><input type="number" name="hkPreis" id="hkPreis"></td>
			</tr>
			<tr>
				<td><label for="vkPreis">Verkaufspreis:</label></td>
				<td><input type="number" name="vkPreis" id="vkPreis"></td>
			</tr>
			<tr>
				<td><label for="erscheinungsJahr">Erscheinungsjahr:</label></td>
				<td><input type="number" name="erscheinungsJahr"
					id="erscheinungsJahr"></td>
			</tr>
		</table>

		<button type="submit">Einfügen</button>
	</form>
	<a href="index.html">Zur Startseite</a>
</body>
</html>