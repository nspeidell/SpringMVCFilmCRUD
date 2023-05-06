<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Film</title>
</head>
<body>
  <form action="EditFilm.do" method="POST">
    <label for="title">Title:</label>
    <input type="text" name="title" value="${film.title }">
    <br>
    <label for="desc">Description:</label>
    <input type="text" name="desc" value="${film.desc}">
    <br>
    <label for="releaseYear">Release Year:</label>
    <input type="number" min="1800" max="2050" name="releaseYear" value="${film.releaseYear}">
    <br>
    <label for="length">Film Length:</label>
    <input type="number" min="0" name="length" value="${film.length}">
    <label for="length">minutes</label>
    <br>
    <label for="rentDur">Rental Duration:</label>
    <input type="number" min="0" name="rentDur" value="${film.rentDur}">
    <label for="rentDur">days</label>
    <br>
    <label for="rate">Rental Rate: $</label>
    <input type="number" name="rate" value="${film.rate}">
    <br>
    <label for="repCost">Replacement Cost: $</label>
    <input type="number" name="repCost" value="${film.repCost}">
    <br>
    <p>Film Rating: ${film.rating}</p>
    <div>
      <label for="rating">Edit Film Rating:</label>
      <input type="radio" id="G" name="rating" value="G">
      <label for="G">G</label>
      <input type="radio" id="PG" name="rating" value="PG">
      <label for="PG">PG</label>
      <input type="radio" id="PG-13" name="rating" value="PG-13">
      <label for="PG-13">PG-13</label>
      <input type="radio" id="R" name="rating" value="R">
      <label for="R">R</label>
      <input type="radio" id="NC-17" name="rating" value="NC-17">
      <label for="NC-17">NC-17</label><br>
    </div>
    <input type="submit" value="Add Film">
  </form>
</body>
</html>