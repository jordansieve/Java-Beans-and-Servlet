<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>CS4010 Project 3 - Keyword Search</title>
	</head>
	<body>
	
		<form method="get" action="KeywordServlet">
			<div style="text-align: center;">
				<input style="width:300px;" type="text" name="keyword" placeholder="Keyword for theory" />
				<input type="submit" value="Search" />
			</div>
		</form>
	
		<br />
		<table style="width:75%; margin-left:auto; margin-right:auto;">
			<tr>
				<th colspan="4" style="background-color:#f5f5f5;">Search Results</th>
			</tr>
			<c:forEach var="keyword" items="${keywordList}">
				<tr>
					<td style="text-align:center;"><input type="submit" value="+" disabled /></td>
					<td style="text-align:center;">${keyword.count}</td>
					<td style="text-align:center;">${keyword.bid}	${keyword.bookTitle}	(${keyword.author})</td>
					<td style="text-align:center;">${keyword.edition} / ${keyword.year}</td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="4" style="background-color: #f5f5f5;">The End</th>
			</tr>
		</table>
	
	</body>
</html>