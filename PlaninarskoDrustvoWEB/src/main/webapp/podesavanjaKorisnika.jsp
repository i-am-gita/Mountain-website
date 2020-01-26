<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix = "form" uri = "http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Podesavanja</title>
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
	
	<div class = "naslov">Zahtevi za učlanjenje u društvo</div>
	<c:if test="${!empty gosti }">
		<table border = 1>
			<tr>
				<th>Ime i prezime</th>
				<th>Korisnicko ime</th>
				<th>Status</th>
			</tr>
			<c:forEach var = "gost" items = "${gosti }">
				<tr>
					<td>${gost.ime } ${gost.prezime}</td>
					<td>${gost.korisnickoIme}</td>
					<td><a href = "/sekretar/potvrdiClanstvo?id=${gost.idKorisnik}">Potvrdi članstvo</a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
		<div class = "naslov">Spisak članova</div>
	<c:if test="${!empty clanovi }">
		<table border = 1>
			<tr>
				<th>Ime i prezime</th>
				<th>Datum isteka clanarine</th>
				<th>Podesavanja</th>
			</tr>
			<c:forEach var = "clan" items = "${clanovi }">
			<tr>
				<td>${clan.ime } ${clan.prezime}</td>
				<td><fmt:formatDate value="${clan.clanarina.do_}" var="datumIsteka"  type="date" pattern="MM-dd-yyyy" />${datumIsteka}</td>
				<td><a href = "/sekretar/produziClanarinu?id=${clan.idKorisnik }">Produži članarinu</a></td>
			</tr>
			</c:forEach>
		</table>
	</c:if>
	<div class = "naslov">Statistike</div>
	<a href = "/sekretar/rezervacije">Prikazi rezervacije</a>
	<br>
	<c:if test="${!empty statistike }">
		<c:forEach var = "statistika" items = "${statistike }">
		<table border = 1>
				<tr>
					<th>Datum početka rezervacije</th>
					<th>Datum kraja rezervacije</th>
					<th>Trajanje rezervacije</th>
				</tr>
			<br>
			<div class = "podnaslov">${statistika.key.naziv }</div>
			<br>
			<c:forEach var = "rezervacija" items = "${statistika.value }">
				<tr>
					<td>${rezervacija.od }</td>
					<td>${rezervacija.do_ }</td>
					<td>${rezervacija.trajanjeRezervacije }</td>
				</tr>
			</c:forEach>
		</c:forEach>
		</table>
		<br>
		<a href = "/sekretar/rezervacije/statistike">Kompletne statistike</a><br>
	</c:if>
</body>
</html>