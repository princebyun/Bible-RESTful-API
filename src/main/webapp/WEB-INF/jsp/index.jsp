<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Worshiping Church Bible</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/webjars/bootstrap/5.3.0/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/css/main.css'/>" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
</head>
<body>

<div class="top-bar">
    <a href="<c:url value='/'/>" class="logo">Worshiping Church Bible</a>
</div>

<div class="main-content">
    <div class="search-wrapper">
        <h1 class="main-title">어떤 말씀을 찾고 계신가요??</h1>
        <form action="<c:url value='/bible'/>" method="get" class="search-form">
            <i class="fas fa-search search-icon"></i>
            <input class="search-input" type="search" name="keyword" placeholder="성경 구절이나 단어를 검색해보세요..."
                   aria-label="Search">
        </form>
    </div>

    <div class="slider-container" id="sliderContainer">
        <button class="slider-btn prev" id="prevBtn">&lt;</button>
        <div class="card-wrapper" id="cardWrapper">
            <div class="card-container" id="cardContainer">
                <!-- 성경 읽기 카드 -->
                <a href="<c:url value='/bible'/>" class="custom-card">
                    <div class="card-image-wrapper">
                        <img src="<c:url value='/resources/images/card_bible.png'/>" alt="성경 검색">
                    </div>
                    <div class="card-content">
                        <div class="card-title">성경 읽기</div>
                        <div class="card-desc">원하는 구절을 찾아<br>말씀을 묵상하세요.</div>
                    </div>
                </a>

                <!-- 오늘의 큐티본문 카드 -->
                <a href="<c:url value='/qt'/>" class="custom-card">
                    <div class="card-image-wrapper">
                        <img src="<c:url value='/resources/images/card_today.png'/>" alt="오늘의 큐티본문">
                    </div>
                    <div class="card-content">
                        <div class="card-title">오늘의 큐티본문</div>
                        <div class="card-desc">매일 주어지는 새로운<br>은혜의 말씀을 만나보세요.</div>
                    </div>
                </a>

                <!-- 주일말씀 카드 -->
                <a href="<c:url value='/sermons'/>" class="custom-card">
                    <div class="card-image-wrapper">
                        <img src="<c:url value='/resources/images/card_pray.png'/>" alt="주일말씀">
                    </div>
                    <div class="card-content">
                        <div class="card-title">주일말씀</div>
                        <div class="card-desc">지난 주일의 은혜로운<br>말씀을 다시 만나보세요.</div>
                    </div>
                </a>

                <!-- 클린챗 검사 카드 -->
                <a href="<c:url value='/cleanbot/chat'/>" class="custom-card">
                    <div class="card-image-wrapper">
                        <img src="<c:url value='/resources/images/card_chat.png'/>" alt="클린챗 검사">
                    </div>
                    <div class="card-content">
                        <div class="card-title">클린챗 검사</div>
                        <div class="card-desc">텍스트의 유해성을<br>검사합니다.</div>
                    </div>
                </a>

                <!-- 클린이미지 검사 카드 -->
                <a href="<c:url value='/cleanbot/image'/>" class="custom-card">
                    <div class="card-image-wrapper">
                        <img src="<c:url value='/resources/images/card_image.png'/>" alt="클린이미지 검사">
                    </div>
                    <div class="card-content">
                        <div class="card-title">클린이미지 검사</div>
                        <div class="card-desc">이미지의 유해성을<br>검사합니다.</div>
                    </div>
                </a>
            </div>
        </div>
        <button class="slider-btn next" id="nextBtn">&gt;</button>
    </div>
</div>

<script src="<c:url value='/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js'/>"></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
    const wrapper = document.getElementById('cardWrapper');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    function getScrollAmount() {
        const screenWidth = window.innerWidth;
        if (screenWidth > 1100) return 320 + 30; // Desktop
        if (screenWidth >= 768) return 300 + 30; // Tablet
        return 280 + 20; // Mobile
    }

    function updateButtons() {
        const screenWidth = window.innerWidth;

        if (screenWidth < 768) {
            prevBtn.style.display = 'none';
            nextBtn.style.display = 'none';
            return;
        }

        // 태블릿 이상에서는 버튼을 flex로 설정
        prevBtn.style.display = 'flex';
        nextBtn.style.display = 'flex';

        const scrollLeft = wrapper.scrollLeft;
        const scrollWidth = wrapper.scrollWidth;
        const clientWidth = wrapper.clientWidth;

        prevBtn.style.opacity = scrollLeft > 0 ? '1' : '0';
        prevBtn.style.visibility = scrollLeft > 0 ? 'visible' : 'hidden';

        const isAtEnd = Math.abs(scrollWidth - clientWidth - scrollLeft) < 1;
        nextBtn.style.opacity = isAtEnd ? '0' : '1';
        nextBtn.style.visibility = isAtEnd ? 'hidden' : 'visible';
    }

    prevBtn.addEventListener('click', () => {
        wrapper.scrollLeft -= getScrollAmount();
    });

    nextBtn.addEventListener('click', () => {
        wrapper.scrollLeft += getScrollAmount();
    });

    wrapper.addEventListener('scroll', updateButtons);
    window.addEventListener('resize', updateButtons);

    // 페이지 로드 시 버튼 상태 초기화
    updateButtons();
});
</script>
</body>
</html>