<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Film Result</title>
</head>
<body>
	<c:choose>
		<c:when test="${! empty film}">
			<h3>${film.title}</h3>
			<ul>
				<li>Film ID: ${film.filmId}</li>
				<c:choose>
					<c:when test="${! empty film.desc}">
						<li>Description: ${film.desc}</li>
					</c:when>
					<c:otherwise>
						<li>Description: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.category}">
						<li>Category: ${film.category}</li>
					</c:when>
					<c:otherwise>
						<li>Category: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.rating}">
						<li>Rating: ${film.rating}</li>
					</c:when>
					<c:otherwise>
						<li>Rating: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.language}">
						<li>Language: ${film.language}</li>
					</c:when>
					<c:otherwise>
						<li>Language: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.releaseYear}">
						<li>Release Year: ${film.releaseYear}</li>
					</c:when>
					<c:otherwise>
						<li>Release Year: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.length}">
						<li>Length: ${film.length} minutes</li>
					</c:when>
					<c:otherwise>
						<li>Length: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.rentDur}">
						<li>Rental Duration: ${film.rentDur} days</li>
					</c:when>
					<c:otherwise>
						<li>Length: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.rate}">
						<li>Rental Rate: $${film.rate}</li>
					</c:when>
					<c:otherwise>
						<li>Rental Rate: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.repCost}">
						<li>Replacement Cost: $${film.repCost}</li>
					</c:when>
					<c:otherwise>
						<li>Replacement Cost: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.features}">
						<li>Special Features: ${film.features}</li>
					</c:when>
					<c:otherwise>
						<li>Special Features: N/A</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${! empty film.cast}">
						<li>Cast: ${film.cast }</li>
					</c:when>
					<c:otherwise>
						<li>This film has no cast.</li>
					</c:otherwise>
				</c:choose>
			</ul>
			<a href="editFilmForm.do?filmId=${film.filmId}">Edit Film Details</a>
			<a href="deleteFilm.do?filmId=${film.filmId}">Delete Film</a>
		</c:when>
		<c:otherwise>
			<p>No film found.</p>
		</c:otherwise>
	</c:choose>
	<br>
	<a href="home.do">Return to Home Menu</a>
</body>
</html>