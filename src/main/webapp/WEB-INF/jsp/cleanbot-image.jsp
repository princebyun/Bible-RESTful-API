<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>클린이미지 검사</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/webjars/bootstrap/5.3.0/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/css/common.css'/>" rel="stylesheet">
    <style>
        .result-safe { border: 2px solid #198754; }
        .result-unsafe { border: 2px solid #dc3545; }
        #imagePreview { max-height: 300px; }
    </style>
</head>
<body>

<%@ include file="menu.jsp" %>

<div class="container mt-4">
    <h1 class="page-title">클린이미지 검사</h1>
    <p class="text-muted">이미지를 업로드하고 유해성 여부를 검사합니다.</p>

    <div class="card">
        <div class="card-body">
            <div class="mb-3">
                <label for="imageInput" class="form-label">이미지 선택</label>
                <input type="file" id="imageInput" class="form-control" accept="image/*" onchange="previewImage()">
            </div>
            <button onclick="checkImage()" class="btn btn-primary">검사하기</button>
        </div>
    </div>

    <div class="mt-4">
        <img id="imagePreview" src="#" alt="이미지 미리보기" class="img-fluid rounded" style="display:none;"/>
    </div>

    <div id="result" class="mt-4"></div>
</div>

<script src="<c:url value='/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js'/>"></script>
<script>
    let originalImageSrc = '';

    function previewImage() {
        const fileInput = document.getElementById('imageInput');
        // const imagePreview = document.getElementById('imagePreview');
        if (fileInput.files.length > 0) {
            const reader = new FileReader();
            reader.onload = (e) => {
                originalImageSrc = e.target.result;
                // 미리보기 주석 처리: 이미지를 선택해도 바로 보여주지 않음
                // imagePreview.src = originalImageSrc;
                // imagePreview.style.display = 'block';
            };
            reader.readAsDataURL(fileInput.files[0]);
        } else {
            // imagePreview.style.display = 'none';
            originalImageSrc = '';
        }
    }

    async function checkImage() {
        const fileInput = document.getElementById('imageInput');
        const resultDiv = document.getElementById('result');
        const imagePreview = document.getElementById('imagePreview');

        if (fileInput.files.length === 0) {
            alert('이미지를 선택해주세요.');
            return;
        }

        // 검사 시작 시 기존 이미지 숨김
        imagePreview.style.display = 'none';
        resultDiv.innerHTML = '<div class="d-flex justify-content-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>';

        const formData = new FormData();
        formData.append('file', fileInput.files[0]);

        try {
            const response = await fetch('/api/cleanbot/check-image', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) throw new Error('서버 오류: ' + response.statusText);

            const result = await response.json();
            let resultHTML;

            if (result.isSafe) {
                imagePreview.src = originalImageSrc; // 원본 이미지 유지
                imagePreview.style.display = 'block'; // 검사 결과가 안전할 때만 이미지 표시

                // JSP EL 충돌 방지를 위해 문자열 연결 사용
                resultHTML = '<div class="card result-safe">' +
                                '<div class="card-header"><strong>검사 결과</strong></div>' +
                                '<div class="card-body">' +
                                    '<p class="card-text text-success"><strong>클린합니다</strong></p>' +
                                    '<p class="card-text"><strong>사유:</strong> ' + result.reason + '</p>' +
                                '</div>' +
                            '</div>';
            } else {
                imagePreview.src = "<c:url value='/resources/images/unsafe_image.png'/>"; // 대체 이미지로 변경
                imagePreview.style.display = 'block'; // 검사 결과가 유해할 때 대체 이미지 표시

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