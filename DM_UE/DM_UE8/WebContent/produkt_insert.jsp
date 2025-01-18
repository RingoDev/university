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
<title>Produkt einfügen</title>
</head>
<body>
	<h1>Produkt einfügen</h1>
	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
		con.setAutoCommit(false);

		// prepare query
		String query = "INSERT INTO produkt (produktNr, gtin, bezeichnung, verkaufspreis, herstellkosten, erscheinungsjahr) VALUES(?,?,?,?,?,?)";

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			// get request parameters
			int prodNr = Integer.parseInt(request.getParameter("produktNr"));
			int gtin = Integer.parseInt(request.getParameter("gtin"));
			String bezeichnung = request.getParameter("bezeichnung");
			double vkPreis = Double.parseDouble(request.getParameter("vkPreis"));
			double hkPreis = Double.parseDouble(request.getParameter("hkPreis"));
			int erscheinungsJahr = Integer.parseInt(request.getParameter("erscheinungsJahr"));

			// bind variables
			stmt.setInt(1, prodNr);
			stmt.setInt(2, gtin);
			stmt.setString(3, bezeichnung);
			stmt.setDouble(4, vkPreis);
			stmt.setDouble(5, hkPreis);
			stmt.setInt(6, erscheinungsJahr);

			int cnt = stmt.executeUpdate();

			// commit the changes
			con.commit();
			if (cnt > 0)
				out.println("Das Produkt mit der Nummer " + prodNr + " wurde erfolgreich erstellt!");
			else
				out.println("Das Produkt mit der Nummer " + prodNr + " konnte nicht erstellt werden!");
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
		<a href="produkt_overview.jsp">Zur Produkt-Übersicht</a>
	</p>
</body>
</html>