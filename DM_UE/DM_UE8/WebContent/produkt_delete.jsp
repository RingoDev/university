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
<title>Produkt löschen</title>
</head>
<body>
	<h1>Produkt löschen</h1>
	<%
		// Get a connection from the DataSource       	
	Context initContext = new InitialContext();
	Context envContext = (Context) initContext.lookup("java:/comp/env");
	DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");

	// open connection
	try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);

			// delete the product with the specified gtin
			try (PreparedStatement stmt = con.prepareStatement("DELETE FROM produkt WHERE produktNr = ?")) {
				// bind variables
				int prodNr = Integer.parseInt(request.getParameter("produktNr"));
				stmt.setInt(1, prodNr);

				int cnt = stmt.executeUpdate();

				// commit the changes
				con.commit();
				if (cnt > 0)
					out.println("Das Produkt mit der Nummer " + prodNr + " wurde erfolgreich gelöscht!");
				else
					out.println("Das Produkt mit der Nummer " + prodNr + " wurde NICHT gelöscht!");
			} catch (SQLException ex) {
				out.println("Fehler: " + ex.getMessage() + " (" + ex.getSQLState() + ')');
				try {
					con.rollback();
				} catch (SQLException ignore) {
					out.println("Rollback nicht erfolgreich! Fehler: " + ignore.getMessage() + " ("
							+ ignore.getSQLState() + ')');
				}
			} catch (Exception ex) {
				out.println("Fehler: " + ex.getMessage());
				try {
					con.rollback();
				} catch (SQLException ignore) {
					out.println("Rollback nicht erfolgreich! Fehler: " + ignore.getMessage() + " ("
							+ ignore.getSQLState() + ')');
				}
			}
		} catch (SQLException ex) {
			out.println("Fehler: " + ex.getMessage() + " (" + ex.getSQLState() + ')');
		}
	%>
	<p><a href="produkt_overview.jsp">Zur Produkt-Übersicht</a></p>
</body>
</html>