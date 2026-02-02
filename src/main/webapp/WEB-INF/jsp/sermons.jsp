<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주일말씀</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/webjars/bootstrap/5.3.0/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/css/common.css'/>" rel="stylesheet">
    <!-- Font Awesome 아이콘 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
    <style>
        /* 비디오 카드 */
        .video-card {
            border: none;
            transition: transform 0.3s, box-shadow 0.3s;
            border-radius: 0.5rem;
            background-color: #ffffff;
        }
        .video-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 20px rgba(0,0,0,0.1) !important;
        }
        .card-img-top {
            aspect-ratio: 16 / 9;
            object-fit: cover;
            border-radius: 0.5rem 0.5rem 0 0;
        }
        .card-title {
            font-size: 0.95rem;
            font-weight: 600;
            color: #343a40;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
            min-height: 2.8em; /* 2줄 높이 확보 */
        }
        .card-footer {
            background-color: transparent;
            border-top: 1px solid #f1f1f1;
        }

        /* 유튜브 버튼 (common.css의 btn-primary 스타일과 유사하게) */
        .btn-youtube {
            background-color: #00BFFF;
            border-color: #00BFFF;
            color: white;
            font-weight: bold;
        }
        .btn-youtube:hover {
            background-color: #009ACD;
            border-color: #009ACD;
            color: white;
        }
    </style>
</head>
<body>

<!-- 공통 메뉴 삽입 -->
<%@ include file="menu.jsp" %>

<div class="container mt-4">

    <!-- 유튜브 채널 바로가기 버튼 -->
    <div class="text-end mb-4">
        <a href="${channelUrl}" target="_blank" class="btn btn-youtube">
            <i class="fab fa-youtube"></i> 전체 영상 보기
        </a>
    </div>

    <c:choose>
        <c:when test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                <i class="fas fa-exclamation-triangle"></i> ${error}
            </div>
        </c:when>
        <c:when test="${empty videos}">
            <div class="alert alert-info text-center py-5">
                <h4><i class="fas fa-info-circle"></i> 최신 영상이 없습니다.</h4>
                <p>유튜브 채널을 확인해주세요.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                <c:forEach var="video" items="${videos}">
                    <div class="col">
                        <a href="${video.link}" target="_blank" class="text-decoration-none">
                            <div class="card h-100 shadow-sm video-card">
                                <img src="${video.thumbnailUrl}" class="card-img-top" alt="${video.title}">
                                <div class="card-body">
                                    <h6 class="card-title">${video.title}</h6>
                                </div>
                                <div class="card-footer text-end">
                                    <small class="text-muted">${video.publishedDate}</small>
                                </div>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="<c:url value='/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js'/>"></script>
</body>
</html>
