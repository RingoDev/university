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
<title>Bestellübersicht</title>
</head>
<body>
	<h1>Bestellungen</h1>
	<a href="index.html">Zur Startseite</a>
	<table border="0">
		<thead>
			<tr>
				<th>Bestell-Nr.</th>
				<th>Positions-Nr.</th>
				<th>Liefer-Nr</th>
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
					ResultSet rs = stmt.executeQuery(
					"SELECT bp.bestellNr, bp.posNr, l.lieferNr,bp.trackingId FROM Bestellposition bp LEFT JOIN Zuordnung z on bp.trackingId = z.trackingId LEFT JOIN Lieferung l on z.lieferNr = l.lieferNr ORDER BY bp.bestellNr,bp.posNr");
					while (rs.next()) {
			%>
			<tr>
				<td><%=rs.getInt("bestellNr")%></td>
				<td><%=rs.getInt("posNr")%></td>
				<td><%=rs.getInt("lieferNr")%></td>
				<td><a
					href="position_delete.jsp?posNr=<%=rs.getInt("posNr")%>&bestellNr=<%=rs.getInt("bestellNr")%>">Löschen</a></td>
			</tr>
			<%
				}
			}
			} catch (SQLException ex) {
			%>
			<tr>
				<td colspan="8" style="color: red;">Die Bestell-Positionen konnten nicht
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