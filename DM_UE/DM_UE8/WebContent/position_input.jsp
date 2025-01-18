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
<title>BestellPosition erstellen</title>
</head>
<body>
	<h1>BestellPosition erstellen</h1>

	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
	%>

	<form action="position_insert.jsp" method="post">
		<table style="border: 0">
			<tr>
				<td><label for="trackingId">TrackingID:</label></td>
				<td><input type="text" name="trackingId" id="trackingId"></td>
			</tr>
			<tr>
				<td><label for="posNr">PositionsNR:</label></td>
				<td><input type="number" name="posNr" id="posNr"></td>
			</tr>
			<tr>
				<td><label for="menge">Menge:</label></td>
				<td><input type="number" name="menge" id="menge"></td>
			</tr>
			<tr>
				<td><label for="bestellEinheit">Bestelleinheit:</label></td>
				<td><select name="bestellEinheit" id="bestellEinheit">
						<option value="Stk">Stück</option>
						<option value="Palette">Palette</option>
						<option value="Karton">Karton</option>
				</select></td>
			</tr>
			<tr>
				<td><label for="einzelPreis">Einzelpreis:</label></td>
				<td><input type="text" name="einzelPreis" id="einzelPreis"></td>
			</tr>
			<tr>
				<td><label for="produkt">Produkt:</label></td>
				<td><select name="produkt" id="produkt">
						<%
							// get all products
						try (Statement stmt = con.createStatement()) {
							ResultSet rs = stmt.executeQuery("SELECT * FROM Produkt p ORDER BY p.produktNr");
							while (rs.next()) {
						%>
						<option value=<%=rs.getInt("produktNr")%>><%=rs.getInt("produktNr")%>
							--
							<%=rs.getString("bezeichnung")%> --
							<%=rs.getString("gtin")%>
						</option>
						<%
							}
						%>
				</select></td>
				<%
					} catch (SQLException ex) {
				%>
				<div style="color: red;">
					Die Produkte konnten nicht abgefragt werden:
					<%=ex.getMessage()%>
					(<%=ex.getSQLState()%>)
				</div>
				<%
					}
				%>


			</tr>
			<tr>
				<td><label for="bestellung">Bestellung:</label></td>
				<td><select name="bestellung" id="bestellung">
						<%
							// get all products
						try (Statement stmt = con.createStatement()) {
							ResultSet rs = stmt.executeQuery("SELECT * FROM Bestellung b ORDER BY b.bestellNr");
							while (rs.next()) {
						%>
						<option value=<%=rs.getInt("bestellNr")%>><%=rs.getInt("bestellNr")%>
							--
							<%=rs.getDate("datum")%>
						</option>
						<%
							}
						} catch (SQLException ex) {
						%>
						<p style="color: red;">
							Die Bestellungen konnten nicht abgefragt werden:
							<%=ex.getMessage()%>
							(<%=ex.getSQLState()%>)
						</p>
						<%
							}
						%>

				</select></td>
			</tr>
		</table>
		<button type="submit">Einfügen</button>
	</form>
	<%
		} catch (SQLException ex) {
	%>
	<p style="color: red;">
		Ein Fehler ist aufgetreten:
		<%=ex.getMessage()%>
		(<%=ex.getSQLState()%>)
	</p>

	<%
		}
	%>
	<a href="index.html">Zur Startseite</a>
</body>
</html>