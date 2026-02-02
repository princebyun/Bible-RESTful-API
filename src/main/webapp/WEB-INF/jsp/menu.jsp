<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="site-header sticky-top">
    <!-- navbar-expand-lg -> navbar-expand-md 변경: 태블릿(768px 이상)에서도 메뉴 펼침 유지 -->
    <nav class="navbar navbar-expand-md navbar-light">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/'/>">Worshiping Church Bible</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/bible'/>">성경 읽기</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/qt'/>">오늘의 큐티본문</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/sermons'/>">주일말씀</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            클린봇
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="<c:url value='/cleanbot/chat'/>">클린챗 검사</a></li>
                            <li><a class="dropdown-item" href="<c:url value='/cleanbot/image'/>">클린이미지 검사</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>
