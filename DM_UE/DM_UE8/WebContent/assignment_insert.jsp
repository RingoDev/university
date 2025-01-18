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
		con.setAutoCommit(false);

		// prepare query
		String query = "INSERT INTO Zuordnung(lieferNr, trackingId, anzahl)" + " VALUES (?, ?, ?)";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			// get request parameters
			int lieferNr = Integer.parseInt(request.getParameter("lieferung"));
			String trackingId = request.getParameter("position");
			int anzahl = Integer.parseInt(request.getParameter("anzahl"));

			// bind variables
			stmt.setInt(1, lieferNr);
			stmt.setString(2, trackingId);
			stmt.setInt(3, anzahl);

			int cnt = stmt.executeUpdate();

			// commit the changes
			con.commit();
			if (cnt > 0)
		out.println("Die Position wurde erfolgreich zugewiesen!");
			else
		out.println("Die Position konnte nicht zugewiesen werden!");
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
		<a href="assignment_input.jsp">Noch eine Position zuweisen</a> <br />
		<a href="index.html">Zur Startseite</a> <br />
	</p>
</body>
</html>