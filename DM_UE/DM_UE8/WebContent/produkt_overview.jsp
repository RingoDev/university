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
<title>Produktübersicht</title>
</head>
<body>
	<h1>Produkte</h1>
	<a href="index.html">Zur Startseite</a>
	<table border="0">
		<thead>
			<tr>
				<th>Produkt-Nr.</th>
				<th>GTIN</th>
				<th>Bezeichnung</th>
				<th>Herstellkosten</th>
				<th>Verkaufspreis</th>
				<th>Erscheinungsjahr</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<%
			// Get a connection from the DataSource       	
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

			// open connection
			try (Connection con = ds.getConnection()) {
				// get all products
				try (Statement stmt = con.createStatement()) {
					ResultSet rs = stmt.executeQuery("SELECT produktNr, gtin, bezeichnung, verkaufspreis, herstellkosten, erscheinungsjahr FROM produkt ORDER BY produktNr");
					while (rs.next()) {
			%>
			<tr>
				<td><%=rs.getInt("produktNr")%></td>
				<td><%=rs.getString("gtin")%></td>
				<td><%=rs.getString("bezeichnung")%></td>
				<td><%=rs.getDouble("herstellkosten")%></td>
				<td><%=rs.getDouble("verkaufspreis")%></td>
				<td><%=rs.getInt("erscheinungsjahr")%></td>
				<td><a href="produkt_delete.jsp?produktNr=<%=rs.getInt(1)%>">Löschen</a></td>
			</tr>
			<%
				}
			}
			} catch (SQLException ex) {
			%>
			<tr>
				<td colspan="8" style="color: red;">Die Produkte konnten nicht
					abgefragt werden: <%=ex.getMessage()%> (<%=ex.getSQLState()%>)
				</td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
	<a href="index.html">Zur Startseite</a>
</body>
</html>