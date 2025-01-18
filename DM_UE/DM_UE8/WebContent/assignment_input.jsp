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
<title>Position zuweisen</title>
</head>
<body>
	<h1>Position zuweisen</h1>

	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
	%>

	<form action="assignment_insert.jsp" method="post">
		<table style="border: 0">
			<tr>
				<td><label for="lieferung">Lieferung:</label></td>
				<td><select name="lieferung" id="lieferung">
						<%
							// get all products
						try (Statement stmt = con.createStatement()) {
							ResultSet rs = stmt.executeQuery("SELECT * FROM Lieferung l ORDER BY l.lieferNr");
							while (rs.next()) {
						%>
						<option value=<%=rs.getInt("lieferNr")%>>
							<%=rs.getString("lieferNr")%>
						</option>
						<%
							}
						%>
				</select></td>
				<%
					} catch (SQLException ex) {
				%>
				<div style="color: red;">
					Die Lieferungen konnten nicht abgefragt werden:
					<%=ex.getMessage()%>
					(<%=ex.getSQLState()%>)
				</div>
				<%
					}
				%>


			</tr>
			<tr>
				<td><label for="position">Bestell-Position:</label></td>
				<td><select name="position" id="position">
						<%
							// get all products
						try (Statement stmt = con.createStatement()) {
							ResultSet rs = stmt.executeQuery("SELECT * FROM BestellPosition bp ORDER BY bp.bestellNr");
							while (rs.next()) {
						%>
						<option value=<%=rs.getString("trackingId")%>>
							Bestellnummer
							<%=rs.getInt("bestellnr")%> -- Position
							<%=rs.getInt("posNr")%>
						</option>
						<%
							}
						} catch (SQLException ex) {
						%>
						<p style="color: red;">
							Die Bestell-Positionen konnten nicht abgefragt werden:
							<%=ex.getMessage()%>
							(<%=ex.getSQLState()%>)
						</p>
						<%
							}
						%>

				</select></td>
			</tr>
			<tr>
				<td><label for="anzahl">Anzahl:</label></td>
				<td><input type="number" name="anzahl" id="anzahl"></td>
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