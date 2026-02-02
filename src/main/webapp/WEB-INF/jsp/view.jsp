<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>성경 보기</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/webjars/bootstrap/5.3.0/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/css/common.css'/>" rel="stylesheet">
</head>
<body>

<!-- 공통 메뉴 삽입 -->
<%@ include file="menu.jsp" %>

<div class="container mt-4">
    <h1 class="page-title">성경 보기</h1>

    <!-- 필터링 폼 -->
    <form id="filterForm" action="/bible" method="get" class="row g-3 align-items-end filter-form">
        <div class="col-md-3">
            <label for="keyword" class="form-label">단어 검색</label>
            <input type="text" id="keyword" name="keyword" class="form-control" value="${selectedKeyword}" placeholder="예: 사랑">
        </div>
        <div class="col-md-2">
            <label for="cate" class="form-label">구분</label>
            <select id="cate" name="cate" class="form-select" onchange="submitFormOnChange()">
                <option value="0" ${selectedCate == null ? 'selected' : ''}>전체</option>
                <c:forEach var="t" items="${testaments}" varStatus="status">
                    <option value="${status.index + 1}" ${selectedCate != null && selectedCate == status.index + 1 ? 'selected' : ''}>${t}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-3">
            <label for="book" class="form-label">성경</label>
            <select id="book" name="book" class="form-select">
                <option value="0" ${selectedBook == null ? 'selected' : ''}>전체</option>
                <c:forEach var="b" items="${books}">
                    <option value="${b.book}" ${selectedBook != null && selectedBook == b.book ? 'selected' : ''}>${b.longLabel}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
            <label for="chapter" class="form-label">장</label>
            <select id="chapter" name="chapter" class="form-select">
                <option value="0" ${selectedChapter == null ? 'selected' : ''}>전체</option>
                <c:if test="${maxChapter > 0}">
                    <c:forEach var="i" begin="1" end="${maxChapter}">
                        <option value="${i}" ${selectedChapter != null && selectedChapter == i ? 'selected' : ''}>${i}장</option>
                    </c:forEach>
                </c:if>
            </select>
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-primary w-100 mt-4">검색</button>
        </div>
    </form>

    <!-- 성경 구절 목록 (아코디언 적용) -->
    <div id="bible-content">
        <c:if test="${empty verses}">
            <div class="alert alert-info">
                검색 결과가 없습니다.
            </div>
        </c:if>

        <c:if test="${not empty verses}">
            <div class="accordion" id="bibleAccordion">
                <c:set var="currentBook" value="-1" />
                <c:forEach var="verse" items="${verses}" varStatus="status">

                    <%-- 책이 바뀌면 새로운 아코디언 아이템 시작 --%>
                    <c:if test="${verse.book != currentBook}">
                        <%-- 첫 번째 아이템이 아니면 이전 그룹을 닫아줌 --%>
                        <c:if test="${currentBook != -1}">
                                </div>
                            </div>
                        </div>
                        </c:if>

                        <%-- 새로운 아코디언 아이템 헤더 --%>
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="heading${verse.book}">
                                <button class="accordion-button ${selectedBook != null ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${verse.book}" aria-expanded="${selectedBook != null ? 'true' : 'false'}" aria-controls="collapse${verse.book}">
                                    ${verse.longLabel}
                                </button>
                            </h2>
                            <%-- 특정 성경을 선택했을 때는 펼쳐서 보여줌 --%>
                            <div id="collapse${verse.book}" class="accordion-collapse collapse ${selectedBook != null ? 'show' : ''}" aria-labelledby="heading${verse.book}">
                                <div class="accordion-body">

                        <c:set var="currentBook" value="${verse.book}" />
                    </c:if>

                    <%-- 구절 내용 --%>
                    <div class="bible-verse">
                        <p>
                            <strong>${verse.chapter}:${verse.paragraph}</strong>
                            <span>${verse.sentence}</span>
                        </p>
                    </div>

                    <%-- 리스트의 마지막이면 태그를 닫아줌 --%>
                    <c:if test="${status.last}">
                            </div>
                        </div>
                    </div>
                    </c:if>
                </c:forEach>
            </div>
        </c:if>
    </div>

</div>

<script src="<c:url value='/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js'/>"></script>
<script>
    document.getElementById('book').addEventListener('change', function() {
        const bookId = this.value;
        const chapterSelect = document.getElementById('chapter');

        // 기존 옵션 초기화 (첫 번째 '전체' 옵션 제외)
        while (chapterSelect.options.length > 1) {
            chapterSelect.remove(1);
        }

        if (bookId && bookId > 0) {
            fetch(`/api/chapters?book=${bookId}`)
                .then(response => response.json())
                .then(data => {
                    const maxChapter = data.maxChapter;
                    for (let i = 1; i <= maxChapter; i++) {
                        const option = document.createElement('option');
                        option.value = i;
                        option.textContent = i + '장';
                        chapterSelect.appendChild(option);
                    }
                })
                .catch(error => console.error('Error fetching chapters:', error));
        }
    });

    function submitFormOnChange() {
        document.getElementById('filterForm').submit();
    }
</script>
</body>
</html>
