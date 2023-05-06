<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta charset="UTF-8">
<title>Deletion Results</title>
</head>
<body>
<c:choose>
<c:when test="${updated } = true">
	<p>Film deletion successful</p>
</c:when>
<c:otherwise>
	<p>Film deletion unsuccessful</p>
</c:otherwise>
</c:choose>
</body>
</html>