<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
<style>
table td {
	vertical-align: top;
	border: solid 1px #888;
	padding: 10px;
}
</style>
</head>
<body>
	<div class="topnav">
	  <div class = "link"><a class="active" href="/pocetna">Pocetna strana</a></div>
	  <div class = "link"><a href="/smestaj/planine">Pronađi smeštaj</a></div>
	  <div class = "link"><a href="/sekcija/prikaz">Slike i izveštaji</a></div>
	  <sec:authorize access="hasRole('sekretar')">
			<div class = "link"><a href="/sekretar/podesavanja">Podešavanja</a></div>
	  </sec:authorize>
	  <sec:authorize access="isAuthenticated()">
	   <div class = "link"> <a href = "/logout">Odjavi se</a></div>
	  </sec:authorize>
	</div>

	<h1>Došlo je do greške!</h1>
	<table>
		<tr>
			<td>Datum</td>
			<td>${timestamp}</td>
		</tr>
		<tr>
			<td>Greška</td>
			<td>${error}</td>
		</tr>
		<tr>
			<td>Status</td>
			<td>${status}</td>
		</tr>
		<tr>
			<td>Poruka</td>
			<td>${message}</td>
		</tr>
		<tr>
			<td>Izuzetak</td>
			<td>${exception}</td>
		</tr>
		<tr>
			<td>Putanja</td>
			<td><pre>${trace}</pre></td>
		</tr>
	</table>
</body>
</html>
</html>