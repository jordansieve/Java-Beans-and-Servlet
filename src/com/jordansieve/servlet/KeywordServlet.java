package com.jordansieve.servlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.swing.text.html.HTMLDocument.Iterator;

import com.jordansieve.javabean.*;

@WebServlet("/KeywordServlet")
public class KeywordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Keyword is used for storing search term from web page
		String keyword = request.getParameter("keyword");
						
		// Try connection to database
		try {
						
			InitialContext ic = new InitialContext();
			DataSource source = (DataSource) ic.lookup("java:comp/env/jdbc/keyworddb");
			Connection connection = source.getConnection();
						
			// Grab first values from database
			PreparedStatement results = connection.prepareStatement("SELECT kid FROM keyword WHERE keyword_name = '" + keyword + "'");
			ResultSet resultsRS = results.executeQuery();
			
			// Reset keyword id
			int kid = 0;
						
			// Get values from result set
			while (resultsRS.next()) {
				kid = resultsRS.getInt("kid");
			}
						
			// Grab correct result set from databases
			results = connection.prepareStatement("SELECT bid, book_title, authors, year_number, edition_number FROM book WHERE bid IN (SELECT source_id FROM my_theory WHERE mtid IN (SELECT item_id FROM single_keyword_mapping WHERE keyword_id = '" + kid + "'))");
			resultsRS = results.executeQuery();
						
			// Create a list of Java beans
			List <KeywordBean> keywordList = new ArrayList<KeywordBean>();
			
			// Put values from the result set
			while (resultsRS.next()) {
				// Create a new Java bean
				KeywordBean keywordBean = new KeywordBean();
				
				// Store data into the bean
				keywordBean.setBookTitle(resultsRS.getString("book_title"));
				keywordBean.setAuthor(resultsRS.getString("authors"));
				keywordBean.setYear(resultsRS.getString("year_number"));
				keywordBean.setEdition(resultsRS.getString("edition_number"));
				keywordBean.setBid(resultsRS.getInt("bid"));
				keywordBean.setKid(kid);
				
				// Add new Java bean to list
				keywordList.add(keywordBean);
			}
			
			// Iterate thru list to get the count
			for (KeywordBean keywordBean: keywordList) {
		    	results = connection.prepareStatement("SELECT COUNT(skmid) as count FROM single_keyword_mapping WHERE item_id IN (SELECT mtid FROM my_theory WHERE source_id='" + keywordBean.getBid() + "') AND keyword_id = '" + keywordBean.getKid() + "'");
		    	resultsRS = results.executeQuery();
		    	while(resultsRS.next()){
		    		keywordBean.setCount(resultsRS.getInt("count"));
		    	}
		    }
			
			// Close all the connections
			results.close();
			resultsRS.close();
			connection.close();
						
			// Forward to the next page
			request.setAttribute("keywordList", keywordList);
		    RequestDispatcher rd = request.getRequestDispatcher("results.jsp");
		    rd.forward(request, response);			
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

}
