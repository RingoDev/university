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
<title>Bestell-Position einfügen</title>
</head>
<body>
	<h1>Bestell-Position einfügen</h1>
	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
		con.setAutoCommit(false);

		// prepare query
		String query = "INSERT INTO Bestellposition(trackingId, bestellNr, posNr, menge, einzelPreis, bestellEinheit, produktNr)"
		+ " VALUES(?,?,?,?,?,?,?)";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			// get request parameters
			String trackingId = request.getParameter("trackingId");
			int bestellNr = Integer.parseInt(request.getParameter("bestellung"));
			int posNr = Integer.parseInt(request.getParameter("posNr"));
			int menge = Integer.parseInt(request.getParameter("menge"));
			double einzelPreis = Double.parseDouble(request.getParameter("einzelPreis"));
			String bestellEinheit = request.getParameter("bestellEinheit");
			int produktNr = Integer.parseInt(request.getParameter("produkt"));

			// bind variables
			stmt.setString(1, trackingId);
			stmt.setInt(2, bestellNr);
			stmt.setInt(3, posNr);
			stmt.setInt(4, menge);
			stmt.setDouble(5, einzelPreis);
			stmt.setString(6, bestellEinheit);
			stmt.setInt(7, produktNr);

			int cnt = stmt.executeUpdate();

			// commit the changes
			con.commit();
			if (cnt > 0)
		out.println("Die Position mit der Bestellnummer " + bestellNr + " und der Positionsnummber " + posNr
				+ " wurde erfolgreich erstellt!");
			else
		out.println("Die Position mit der Bestellnummer " + bestellNr + " und der Positionsnummber " + posNr
				+ " konnte nicht erstellt werden!");
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