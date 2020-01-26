<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href = "/dizajn/style.css">
<meta charset="UTF-8">
<title>Registracija</title>
</head>
<body>
<form class = "forma" action="/register" method="post">
		<center>
			<h2>Registracija</h2>
			<label for="name">Unesite Vaše ime</label>
				<input type="text" id="name" name="name" /> 
			<label for="prezime">Unesite Vaše prezime</label>
				<input type="text" id="lastname" name="lastname" /> 
			<label for="username">Izaberite korisnicko ime</label> 
				<input type="text" id="username" name="username" /> 
			<label for="password">Izaberite lozinku</label>
				<input type="password" id="password" name="password" />
				<button type="submit" class="btn">Registruj se</button>
				<small><a href = "/index.jsp">Nazad</a></small>
		</center>
	</form>
</body>
</html>