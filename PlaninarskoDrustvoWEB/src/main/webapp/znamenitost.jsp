<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href = "/dizajn/style.css">
<meta charset="UTF-8">
<title>Znamenitost</title>
</head>
<body>
	
	<div class="topnav">
	 <a class="active" href="/clan/pocetna">Pocetna strana</a>
	 <a href="/smestaj/planine">Pronađi smeštaj</a>
	 <a href="/sekcija/prikaz">Slike i izveštaji</a>
	  <sec:authorize access="hasRole('sekretar')">
			<a href="/sekretar/podesavanja">Podešavanja</a>
			<a href="/sekretar/znamenitost/spisak?idZnamenitost=${znamenitost.idZnamenitost }">Spisak rezervacija</div>
	  </sec:authorize>
	  <sec:authorize access="isAuthenticated()">
	  		<a href = "/logout">Odjavi se</a>
	  </sec:authorize>
	</div>
	
	<c:if test="${!empty znamenitost }">
	<div class = "znamenitost">
		<div class = "naslov">Opis</div>
		<br>
		<div class = "tekst">${znamenitost.opis }</div>
		<br>
		<div class = "naslov">Slike</div>
		<br>
		<c:forEach var="slika" items="${znamenitost.slikas }">
			<div class = "slika"><img src="/clan/znamenitost/slika?idSlike=${slika.idSlika}"></div>
		</c:forEach>
		<br>
		<div class = "naslov">Rezervacija</div>
		<br>
		<c:if test="${!empty termini }">
			<form action="/clan/znamenitost/rezervisi">
				<select name="terminPosete">
					<c:forEach var="trmn" items="${znamenitost.terminZnamenitosts }">
						<option value="${trmn.idTermin }">${trmn.pocetak }|${trmn.kraj }</option>
					</c:forEach>
				</select>
				<button type="submit" class="btn">Rezervisi obilazak</button>
				<br>
			</form>
			<c:if test="${rezervisao eq 'jeste' }">
				<div class ="povratna-poruka">Uspešno ste rezervisali posetu ovoj znamenitosti.</div>
			</c:if>
			<c:if test="${rezervisao eq 'nije' }">
				<div class ="povratna-poruka">Nije moguće rezervisati posetu ovoj znamenitosti zato što za vreme ovog termina niste gost nijednog doma na planini na kojoj se ova znamenitost nalazi.</div>
			</c:if>
		</c:if>
		<c:if test="${empty termini }">
			<form action=/clan/znamenitost/poseti>
				Datum i vreme kada želite da posetite znamenitost:<input type="date"
					name="terminP"><br> <input type="hidden"
					value="${znamenitost.idZnamenitost }" name="znamenitost">
				<button type="submit" class="btn">Izaberi</button>
				<br>
			</form>
			<c:if test="${mogucaPoseta eq 'nije' }"> 
				<div class ="povratna-poruka">Ne možete posetiti ovu znamenitost zato što za vreme ovog termina niste gost nijednog doma na planini na kojoj se ova znamenitost nalazi</div>
			</c:if>
		</c:if>
		<div class = "naslov">Komentari</div>
		<br>
		<c:forEach var="termin" items="${znamenitost.terminZnamenitosts }">
			<c:forEach var="poseta" items="${termin.posetas }">
				<c:forEach var="komentar" items="${poseta.komentars }">
					<div class = "komentar">${komentar.tekst}</div>
					<br>
				</c:forEach>
			</c:forEach>
		</c:forEach>

		<form action="/clan/komentarisi">
			Unesite vaš komentar:
			<br>
			<textarea rows="1" cols="1" name="tekst"></textarea>
			<br> 
			<input type="hidden" value="${znamenitost.idZnamenitost }" name="idZnamenitost">
			<button type="submit" class="btn">Objavi</button>
			<br>
		</form>
		<c:if test="${posetio eq 'nije' }">
			<div class = "povratna-poruka">Ne možete komentarisati ovu znamenitost jer je niste posetili!</div>
		</c:if>
	</c:if>
</body>
</html>