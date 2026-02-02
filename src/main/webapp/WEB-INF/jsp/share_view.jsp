<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>공유된 큐티</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body, html {
            margin: 0;
            padding: 0;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #f0f0f0;
        }

        img {
            max-width: 100%;
            max-height: 100%;
            object-fit: contain;
        }
    </style>
</head>
<body>
<img src="<c:url value='/share/${requestScope["javax.servlet.forward.servlet_path"].substring(12)}'/>"
     alt="매일 새벽1시에 이미지를 삭제합니다.">
</body>
</html>
