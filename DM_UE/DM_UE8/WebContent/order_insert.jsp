<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Date"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Bestellung einfügen</title>
</head>
<body>
	<h1>Bestellung einfügen</h1>
	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
		con.setAutoCommit(false);

		// prepare query
		String query = "INSERT INTO Bestellung(bestellNr, datum) VALUES(?,TO_DATE(?,'yyyy-mm-dd'))";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			// get request parameters
			int bestellNr = Integer.parseInt(request.getParameter("bestellNr"));
			String datum = request.getParameter("datum");

			// bind variables
			stmt.setInt(1, bestellNr);
			stmt.setString(2, datum);

			int cnt = stmt.executeUpdate();

			// commit the changes
			con.commit();
			if (cnt > 0)
				out.println("Die Bestellung mit der Nummer " + bestellNr + " wurde erfolgreich erstellt!");
			else
				out.println("Die Bestellung mit der Nummer " + bestellNr + " konnte nicht erstellt werden!");
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
		<a href="position_input.jsp">Bestell-Position erstellen</a> <br/>
		<a href="position_overview.jsp">Zur Bestellungs-Übersicht</a>
	</p>
</body>
</html>