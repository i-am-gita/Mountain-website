<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix = "form" uri = "http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href = "/dizajn/style.css">
<meta charset="UTF-8">
<title>Smeštaj</title>
</head>
<body>
	<div class="topnav">
	 <a class="active" href="/clan/pocetna">Pocetna strana</a>
	 <a href="/smestaj/planine">Pronađi smeštaj</a>
	 <a href="/sekcija/prikaz">Slike i izveštaji</a>
	  <sec:authorize access="hasRole('sekretar')">
			<a href="/sekretar/podesavanja">Podešavanja</a>
	  </sec:authorize>
	  <sec:authorize access="isAuthenticated()">
	  		<a href = "/logout">Odjavi se</a>
	  </sec:authorize>
	</div>
	
	<c:if test="${!empty planine }">
		<form class = "forma" action = "/smestaj/domovi" method = "get">
		<center>
			Izaberite planinu za koju želite da vidite smeštaje:
			<select name = "planina">
				<c:forEach var = "planina" items = "${planine}">
					<option value = "${planina.idPlanina }">${planina.naziv }</option>
				</c:forEach>
			</select>
			<button type="submit" class="btn">Prikazi smestaj</button><br>
		</center>
		</form>
	</c:if>
	<c:if test="${!empty domovi }">
		<table border = 1>
			<tr>
				<th>Planinarski dom</th>
				<th>Broj slobodnih mesta</th>
				<th>Planinarske staze</th>
				<th>Datum rezervacije</th>
				<th>Rezervisi</th>
			</tr>
			<c:forEach var = "dom" items = "${domovi}">
				<tr>
					<td>${dom.naziv }</td>
					<td>
						<c:forEach var = "mestaDom" items = "${slobodnaMesta }">
							<c:if test="${mestaDom.key eq dom.idDom }">${mestaDom.value }</c:if>
						</c:forEach>
					</td>
					<td><a class = "btn" href = "/planina/staze?idPlanine=${dom.planina.idPlanina }">Prikazi planinarske staze</a></td>
					<form action = "/clan/rezervacija" method = "post">
						<td>Dolazak: <input type = "date" name = "datumOd" required> Odlazak: <input type = "date" name = "datumDo" required></td>
						<input type = "hidden" name = "idDom" value = "${dom.idDom }">
						<td><button type="submit" class="btn">Rezervisi</button></td>
					</form>
				</tr>
				
				<c:if test="${status eq 'uspeh' }"><div class = "povratna-poruka">Uspesno ste rezervisali mesto u domu!</div></c:if>
				<c:if test="${status eq 'greska' }"><div class = "povratna-poruka">Niste izabrali odgovarajuće datume!</div></c:if>
				
			</c:forEach>
		</table>
	</c:if>
	
	<c:if test="${!empty staze }">
		<div class = "znamenitost">
			<c:forEach var = "staza" items = "${staze }">
				<div class = "naslov">Naziv</div>
				<br>
				<div class = "tekst">${staza.naziv }</div>
				<br>
				<div class = "naslov">Opis</div>
				<br>
				<div class = "tekst">${staza.opis }</div>
				<br>
				<div class = "naslov">Tezina</div>
				<br>
				<div class = "tekst">${staza.tezina }</div>
				<br>
				<div class = "naslov">Mapa</div>
				<br>
				<div class = "slika tekst"><img src = "/planina/staza/slika?idStaza=${staza.idStaza}"></div>
				<br>
				<div class = "naslov">Znamenitosti</div>
				<br>
				<c:forEach	var = "znamenitost" items = "${znamenitosti[staza.idStaza] }">
					<div class = "link tekst"><a href = "/clan/znamenitost?idZnamenitost=${znamenitost.idZnamenitost }">${znamenitost.tip }</a></div>  
				</c:forEach>
				<br>
				<hr>
				<br>
			</c:forEach>
		</div>
	</c:if>
</body>
</html>