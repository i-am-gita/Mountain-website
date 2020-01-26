<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix = "form" uri = "http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sekcija</title>
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
	
	<div class = "tekst">Ovde možete podeliti vaša iskustva sa drugim planinarima u vidu slika i izveštaja.</div>
	<br>
		<form action = "/sekcija/postavi" method = "post" enctype = "multipart/form-data">
	      	 Izaberite planinu za koju želite da objavite utiske:
	      	 <br>
	      	 <select name = "idPlanina">
	      	 	<c:forEach var = "pln" items = "${svePlanine }">
	      	 		<option value = "${pln.idPlanina }">${pln.naziv}</option>
	      	 	</c:forEach>
	      	 </select>
	      	 <br>
	      	 Izveštaj sa planinarenja:
	      	 <br>
	      	 <textarea rows="1" cols="1" name = "tekst"></textarea>
	      	 <br>
	      	 Izaberite sliku koju želite da postavite:
	      	 <br>
	         <input type = "file" name = "slika" size = "50" />
	         <br>
	         <button type="submit" class="btn">Objavi</button>
	      </form>
	      
	      
	      <c:if test="${!empty podaci }">
	      	<c:forEach var = "planina" items = "${svePlanine}">
	      		<div class = "podnaslov">${planina.naziv }</div> 
	      		<br>
	      		<c:if test="${!empty podaci[planina.idPlanina] }">
	      			<c:forEach var = "izvestaj" items = "${podaci[planina.idPlanina]}">
	      				<div class = "tekst">${izvestaj.tekst}</div>
	      				<br>
	      				<c:forEach var = "slika" items = "${izvestaj.slikas }">
	      					<div class = "slika"><img src = "/sekcija/slika?idSlike=${slika.idSlika}"></div>
	      				</c:forEach>
	      			</c:forEach>
		      	</c:if>
	      	</c:forEach>
	      </c:if>
</body>
</html>