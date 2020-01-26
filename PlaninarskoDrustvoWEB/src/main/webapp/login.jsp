<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href = "/dizajn/style.css">
<meta charset="UTF-8">
<title>Hello</title>
</head>
<body>
	<form class = "forma" th:action="@{/login}" method="post">
		<center> 
			<input type = "hidden" name = "${_csrf.parameterName}" value = ${_csrf.token }"/>
			<legend>Ulogujte se</legend>
			<label for="username">Korsničko ime</label> 
			<input type="text" id="username" name="username" /> 
			<br>
			<label for="password">Lozinka</label>
			<input type="password" id="password" name="password" />
			<br>
			<button type="submit" class="btn">Log in</button>
		</center> 
	</form>
</body>
</html>