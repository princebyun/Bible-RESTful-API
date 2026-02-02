<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>오늘의 큐티본문</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/webjars/bootstrap/5.3.0/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/css/common.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/css/qt.css'/>" rel="stylesheet">
    <!-- html2canvas 라이브러리 -->
    <script src="https://html2canvas.hertzen.com/dist/html2canvas.min.js"></script>
    <!-- Kakao SDK (integrity 속성 제거) -->
    <script src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.0/kakao.min.js" crossorigin="anonymous"></script>
    <style>
        /* 카카오 버튼 스타일 */
        .btn-kakao {
            background-color: #FEE500;
            color: #000000;
            border: none;
            font-weight: bold;
        }

        .btn-kakao:hover {
            background-color: #FDD835;
            color: #000000;
        }
    </style>
</head>
<body>

<!-- 공통 메뉴 삽입 -->
<%@ include file="menu.jsp" %>

<div class="qt-page-container">
    <c:choose>
        <c:when test="${not empty error}">
            <div class="alert alert-danger mt-4" role="alert">
                    ${error}
            </div>
        </c:when>
        <c:otherwise>
            <!-- 캡처 대상 영역에 ID 부여 -->
            <div class="row g-3 h-100" id="capture-area">
                <!-- 왼쪽: 큐티 본문 -->
                <div class="col-md-6 h-100">
                    <div class="qt-content-area">
                        <div class="qt-header">
                            <p class="qt-date">${date}</p>
                            <h1 class="qt-title">${title}</h1>
                            <p class="qt-passage">${passage}</p>
                        </div>
                        <div class="qt-body">
                            <c:forEach var="verse" items="${verses}">
                                <div class="verse">
                                    <p><c:out value="${verse}" escapeXml="false"/></p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <!-- 오른쪽: 묵상 노트 -->
                <div class="col-md-6 h-100">
                    <div class="qt-note-area">
                        <h2 class="note-title">나의 묵상</h2>
                        <textarea class="note-textarea" placeholder="오늘 말씀을 통해 받은 은혜를 기록해보세요..."></textarea>
                    </div>
                </div>
            </div>

            <!-- 버튼 영역 -->
            <div class="btn-area text-end">
                <button class="btn btn-primary btn-sm me-2" onclick="saveImage()">
                    <i class="fas fa-download"></i> 이미지로 저장하기
                </button>
                <button class="btn btn-kakao btn-sm" onclick="shareKakao()">
                    <i class="fas fa-comment"></i> 카카오톡으로 공유하기
                </button>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="<c:url value='/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js'/>"></script>
<script>
    // 카카오 SDK 초기화
    try {
        Kakao.init('58f34b0958d81c971284547077722431');
    } catch (e) {
        console.error("Kakao SDK 초기화 실패. 키를 확인하세요.");
    }

    // 공통 캡처 함수
    function captureScreen() {
        const captureArea = document.getElementById("capture-area");
        return html2canvas(captureArea, {
            scale: 2,
            backgroundColor: "#f0f8ff",
            logging: false,
            useCORS: true,
            onclone: (clonedDoc) => {
                // 복제된 DOM에서 textarea를 찾아 div로 변환 (줄바꿈 유지)
                const textareas = clonedDoc.querySelectorAll('.note-textarea');
                textareas.forEach(textarea => {
                    const div = clonedDoc.createElement('div');

                    // 원본 textarea의 클래스와 스타일을 최대한 유지
                    div.className = textarea.className;
                    div.style.whiteSpace = 'pre-wrap'; // 줄바꿈과 공백을 유지
                    div.style.height = 'auto';         // 내용에 따라 높이 자동 조절
                    div.style.overflow = 'visible';    // 내용이 잘리지 않도록
                    div.style.resize = 'none';         // 리사이즈 핸들 제거
                    div.style.border = 'none';         // 테두리 제거 (필요시)

                    // placeholder가 보이지 않도록
                    div.removeAttribute('placeholder');

                    // 내용을 그대로 복사
                    div.innerText = textarea.value;

                    // 복제된 DOM에서 textarea를 생성한 div로 교체
                    textarea.parentNode.replaceChild(div, textarea);
                });
            }
        });
    }

    // 1. 이미지 저장 함수
    function saveImage() {
        captureScreen().then(canvas => {
            const image = canvas.toDataURL("image/png");
            const link = document.createElement("a");
            link.href = image;
            link.download = "오늘의큐티_" + new Date().toISOString().slice(0, 10) + ".png";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }).catch(err => {
            console.error("이미지 저장 실패:", err);
            alert("이미지 저장에 실패했습니다.");
        });
    }


    // 2. 카카오톡 공유 함수 (서버 업로드 + 카카오 업로드)
    function shareKakao() {
        if (!Kakao.isInitialized()) {
            alert('카카오 JavaScript 키가 설정되지 않았습니다. 코드를 확인해주세요.');
            return;
        }

        captureScreen().then(canvas => {
            // 1. 우리 서버 업로드용 데이터 (Base64 String)
            const imageDataUrl = canvas.toDataURL('image/png');

            // 2. 카카오 서버 업로드용 데이터 (File Object)
            canvas.toBlob(blob => {
                const file = new File([blob], "qt_share.png", {type: "image/png"});

                // 두 업로드 작업을 병렬로 처리
                Promise.all([
                    // A. 우리 서버에 이미지 업로드 (공유 페이지 링크 생성용)
                    fetch('/uploadImage', {
                        method: 'POST',
                        // 컨트롤러가 @RequestBody String으로 받으므로 JSON.stringify 없이 보냄
                        body: imageDataUrl
                    }).then(res => {
                        if (!res.ok) throw new Error('Server upload failed');
                        return res.json();
                    }),

                    // B. 카카오 서버에 이미지 업로드 (카카오톡 썸네일용)
                    Kakao.Share.uploadImage({
                        file: [file]
                    })
                ])
                    .then(([serverData, kakaoData]) => {
                        // 두 업로드가 모두 성공했을 때 공유 메시지 전송
                        if (serverData.url && kakaoData.infos.original.url) {
                            const shareUrl = window.location.origin + serverData.url;
                            const kakaoImageUrl = kakaoData.infos.original.url;
                            const width = canvas.width;
                            const height = canvas.height;

                            Kakao.Share.sendDefault({
                                objectType: 'feed',
                                content: {
                                    title: '${title}',
                                    description: '${date} 묵상 나눔',
                                    imageUrl: kakaoImageUrl, // 카카오 서버에 올라간 이미지 사용
                                    imageWidth: width,
                                    imageHeight: height,
                                    link: {
                                        mobileWebUrl: shareUrl, // 클릭 시 이동할 우리 서버 페이지
                                        webUrl: shareUrl
                                    }
                                },
                                buttons: [
                                    {
                                        title: '묵상 보기',
                                        link: {
                                            mobileWebUrl: shareUrl,
                                            webUrl: shareUrl
                                        }
                                    }
                                ]
                            });
                        } else {
                            throw new Error('업로드 응답이 올바르지 않습니다.');
                        }
                    })
                    .catch(error => {
                        console.error('공유 처리 중 오류:', error);
                        alert('카카오톡 공유에 실패했습니다.');
                    });

            });
        }).catch(err => {
            console.error("화면 캡처 실패:", err);
            alert("화면 캡처에 실패했습니다.");
        });
    }
</script>
</body>
</html>
