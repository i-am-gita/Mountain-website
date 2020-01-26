<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href = "/dizajn/style.css">
<title>Pocetna strana</title>
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

	<form:form action="${pageContext.request.contextPath}/logout"
		method="POST">
		<input type="hidden" name="${_csrf.parameterName}"
			value=${_csrf.token } "/>
		<input type="submit" value="Odjavi se" />
	</form:form>

	<div class = "content"><h1>Dobrodošao ${korisnickoIme}</h1>
	<c:if test="${uloga.naziv eq 'gost' }">, zahtev za učlanjenje u naše društvo je prosleđen sekretaru.</c:if>
	</div>
</body>
</html>