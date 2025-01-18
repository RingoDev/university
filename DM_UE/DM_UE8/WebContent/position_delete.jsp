<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Bestellposition löschen</title>
</head>
<body>
	<h1>Bestellposition löschen</h1>
	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
		con.setAutoCommit(false);

		// delete the product with the specified gtin
		try (PreparedStatement stmt = con
		.prepareStatement("DELETE FROM Bestellposition WHERE bestellNr = ? AND posNr = ?")) {
			// bind variables
			int bestellNr = Integer.parseInt(request.getParameter("bestellNr"));
			int posNr = Integer.parseInt(request.getParameter("posNr"));
			stmt.setInt(1, bestellNr);
			stmt.setInt(2, posNr);

			int cnt = stmt.executeUpdate();

			// commit the changes
			con.commit();
			if (cnt > 0)
		out.println("Die Position mit der Bestellnummer " + bestellNr + " und der Positionsnummer " + posNr
				+ " wurde erfolgreich gelöscht!");
			else
		out.println("Die Position mit der Bestellnummer " + bestellNr + " und der Positionsnummer " + posNr
				+ " wurde NICHT gelöscht!");
		} catch (SQLException ex) {
			out.println("Fehler: " + ex.getMessage() + " (" + ex.getSQLState() + ')');
			try {
		con.rollback();
			} catch (SQLException ignore) {
		out.println(
				"Rollback nicht erfolgreich! Fehler: " + ignore.getMessage() + " (" + ignore.getSQLState() + ')');
			}
		} catch (Exception ex) {
			out.println("Fehler: " + ex.getMessage());
			try {
		con.rollback();
			} catch (SQLException ignore) {
		out.println(
				"Rollback nicht erfolgreich! Fehler: " + ignore.getMessage() + " (" + ignore.getSQLState() + ')');
			}
		}
	} catch (SQLException ex) {
		out.println("Fehler: " + ex.getMessage() + " (" + ex.getSQLState() + ')');
	}
	%>
	<p>
		<a href="position_overview.jsp">Zur Bestell-Positions Übersicht</a>
	</p>
</body>
</html>