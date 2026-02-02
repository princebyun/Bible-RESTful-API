<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>클린챗 검사</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/webjars/bootstrap/5.3.0/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/css/common.css'/>" rel="stylesheet">
    <style>
        .result-safe { border: 2px solid #198754; }
        .result-unsafe { border: 2px solid #dc3545; }
    </style>
</head>
<body>

<%@ include file="menu.jsp" %>

<div class="container mt-4">
    <h1 class="page-title">클린챗 검사</h1>
    <p class="text-muted">텍스트를 입력하고 유해성 여부를 검사합니다.</p>

    <div class="card">
        <div class="card-body">
            <div class="mb-3">
                <label for="textInput" class="form-label">검사할 텍스트</label>
                <textarea id="textInput" class="form-control" rows="8" placeholder="여기에 텍스트를 입력하세요..."></textarea>
            </div>
            <button onclick="checkText()" class="btn btn-primary">검사하기</button>
        </div>
    </div>

    <div id="result" class="mt-4"></div>
</div>

<script src="<c:url value='/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js'/>"></script>
<script>
    async function checkText() {
        const text = document.getElementById('textInput').value;
        const resultDiv = document.getElementById('result');
        resultDiv.innerHTML = '<div class="d-flex justify-content-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>';

        try {
            const response = await fetch('/api/cleanbot/check', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ text: text })
            });

            if (!response.ok) throw new Error('서버 오류: ' + response.statusText);

            const result = await response.json();
            let resultHTML;

            if (result.isSafe) {
                // JSP EL 충돌 방지를 위해 문자열 연결 사용
                resultHTML = '<div class="card result-safe">' +
                                '<div class="card-header"><strong>검사 결과</strong></div>' +
                                '<div class="card-body">' +
                                    '<p class="card-text text-success"><strong>클린합니다</strong></p>' +
                                    '<p class="card-text"><strong>사유:</strong> ' + result.reason + '</p>' +
                                '</div>' +
                            '</div>';
            } else {
                // JSP EL 충돌 방지를 위해 문자열 연결 사용
                resultHTML = '<div class="card result-unsafe">' +
                                '<div class="card-header"><strong>검사 결과</strong></div>' +
                                '<div class="card-body">' +
                                    '<p class="card-text text-danger"><strong>부적절합니다</strong></p>' +
                                    '<p class="card-text"><strong>사유:</strong> ' + result.reason + '</p>' +
                                '</div>' +
                            '</div>';
            }
            resultDiv.innerHTML = resultHTML;

        } catch (error) {
            resultDiv.innerHTML = '<div class="alert alert-danger" role="alert"><strong>오류:</strong> ' + error.message + '</div>';
        }
    }
</script>
</body>
</html>