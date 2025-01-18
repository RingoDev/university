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
<title>Lieferung einfügen</title>
</head>
<body>
	<h1>Lieferung einfügen</h1>
	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
		con.setAutoCommit(false);

		// prepare query
		String query = "INSERT INTO Lieferung(lieferNr, lieferZeitpunkt)" + " VALUES (?, TO_DATE(?, 'YYYY-MM-DD HH24:MI'))";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			// get request parameters
			int lieferNr = Integer.parseInt(request.getParameter("lieferNr"));
			String lieferZeitpunkt = request.getParameter("lieferZeitpunkt").replace("T"," ");

		// bind variables
	stmt.setInt(1, lieferNr);
	stmt.setString(2, lieferZeitpunkt);


	int cnt = stmt.executeUpdate();

	// commit the changes
	con.commit();
	if (cnt > 0)
		out.println("Die Lieferung mit der Nummer " + lieferNr + " wurde erfolgreich erstellt!");
	else
		out.println("Die Lieferung mit der Nummer " + lieferNr + " konnte nicht erstellt werden!");
	} catch (SQLException ex) {
	out.println("Fehler: " + ex.getMessage() + " (" + ex.getSQLState() + ')');
	try {
		con.rollback();
	} catch (SQLException ignore) {
		out.println("Rollback nicht erfolgreich! Fehler: " + ignore.getMessage() + " (" + ignore.getSQLState() + ')');
	}
	} catch (Exception ex) {
	out.println("Fehler: " + ex.getMessage());
	try {
		con.rollback();
	} catch (SQLException ignore) {
		out.println("Rollback nicht erfolgreich! Fehler: " + ignore.getMessage() + " (" + ignore.getSQLState() + ')');
	}
	}
	} catch (SQLException ex) {
	out.println("Fehler: " + ex.getMessage() + " (" + ex.getSQLState() + ')');
	}
	%>
	<p>
		<a href="delivery_input.jsp">Noch eine Lieferung erstellen</a> <br /> 
		<a href="position_overview.jsp">Zur Bestellungs-Übersicht</a> <br/>
		<a href=".jsp">Zur Startseite</a> <br/>
	</p>
</body>
</html>