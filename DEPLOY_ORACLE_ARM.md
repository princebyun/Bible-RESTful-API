# Oracle Cloud ARM 서버 Docker 설치 및 Bible 앱 실행 가이드

Oracle Cloud에서 **Ampere A1(ARM)** 인스턴스를 사용 중일 때, Docker 설치부터 백엔드/프론트엔드 실행까지 단계별로 진행하는 방법입니다.

---

## 0. 사전 확인

- **OS**: Ubuntu 22.04 / 24.04 권장 (Ampere 이미지 선택 시 기본)
- **SSH 접속**: `ssh -i your-key.key ubuntu@공인IP` (또는 opc 사용 시 `opc@공인IP`)
- **Oracle Cloud 보안 목록**: 인바운드 규칙에 **80(HTTP)**, **8080(API)** 포트 허용 필요 (아래 7장 참고)

---

## 1. Oracle Cloud ARM 서버에 Docker 설치

### 1-1. 패키지 업데이트 및 필수 패키지

```bash
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg
```

### 1-2. Docker 공식 GPG 키 및 저장소 추가

```bash
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

### 1-3. Docker 엔진 설치

```bash
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

### 1-4. 현재 사용자에게 Docker 실행 권한 부여 (선택)

```bash
sudo usermod -aG docker $USER
```

적용하려면 **로그아웃 후 다시 SSH 접속**하거나, 당장은 `sudo docker` 로 실행하면 됩니다.

### 1-5. 설치 확인

```bash
sudo docker run hello-world
```

`Hello from Docker!` 메시지가 나오면 정상입니다. ARM 이미지도 자동으로 받아집니다.

---

## 2. Docker Compose 설치 확인 (이미 포함됨)

위 1-3에서 `docker-compose-plugin` 을 설치했으므로 다음으로 확인합니다.

```bash
sudo docker compose version
```

---

## 3. 백엔드·프론트엔드 이미지 준비 (로컬 PC 또는 서버에서)

이미지는 **ARM 서버에서 직접 빌드**하거나, **로컬(Windows)에서 ARM용으로 빌드**한 뒤 서버로 옮길 수 있습니다.  
ARM 서버에서 빌드하면 가장 단순합니다.

### 방법 A: ARM 서버에서 직접 빌드 (권장)

#### 3-A-1. 프로젝트 코드를 서버로 복사

로컬 PC에서 SCP 또는 Git으로 서버에 백엔드/프론트 폴더를 둡니다.

```bash
# 예: 로컬에서 서버로 복사 (로컬 PowerShell 또는 CMD)
scp -i your-key.key -r D:\princebyun\backend\Bible-RESTful-API ubuntu@공인IP:~/
scp -i your-key.key -r D:\princebyun\frontend\Bible-React ubuntu@공인IP:~/
```

또는 서버에서 Git clone:

```bash
cd ~
git clone <백엔드-저장소-url> Bible-RESTful-API
git clone <프론트-저장소-url> Bible-React
```

#### 3-A-2. 백엔드 WAR 빌드 후 Docker 이미지 빌드 (서버에서)

서버에 **Java 17**이 필요합니다. 없으면 먼저 설치합니다.

```bash
sudo apt-get install -y openjdk-17-jdk
```

이후 WAR 빌드 및 이미지 빌드:

```bash
cd ~/Bible-RESTful-API
chmod +x gradlew
./gradlew war
sudo docker build -t bible-api .
```

#### 3-A-3. 프론트엔드 Docker 이미지 빌드 (서버에서)

**중요**: `VITE_API_BASE_URL` 에는 **사용자가 접속할 백엔드 주소**를 넣습니다.  
브라우저가 접속하는 주소이므로 **서버의 공인 IP** 또는 **도메인**을 사용해야 합니다.

```bash
cd ~/Bible-React
sudo docker build --build-arg VITE_API_BASE_URL=http://공인IP:8080 -t bible-react .
```

도메인을 쓰면 예:

```bash
sudo docker build --build-arg VITE_API_BASE_URL=https://api.도메인.com -t bible-react .
```

---

### 방법 B: 로컬(Windows)에서 ARM 이미지 빌드 후 서버로 전송

로컬에 Docker Desktop이 있고 **Buildx**로 ARM 빌드가 가능하다면:

```bash
# 백엔드 (로컬에서 WAR 빌드 후)
cd D:\princebyun\backend\Bible-RESTful-API
.\gradlew war
docker buildx build --platform linux/arm64 -t bible-api:arm64 --load .

# 프론트 (로컬에서)
cd D:\princebyun\frontend\Bible-React
docker buildx build --platform linux/arm64 --build-arg VITE_API_BASE_URL=http://공인IP:8080 -t bible-react:arm64 --load .
```

이미지를 tar로 저장한 뒤 서버로 SCP로 보내고, 서버에서 `docker load` 로 불러올 수 있습니다.  
일반적으로는 **방법 A(서버에서 빌드)** 가 더 간단합니다.

---

## 4. 백엔드 실행 (Oracle Cloud ARM 서버)

### 4-1. 환경 변수·설정 (필요 시)

- DB: `application.properties` 에 있는 DB URL/계정이 ARM 서버에서 접근 가능한지 확인 (같은 VCN 내부 RDS 또는 외부 DB).
- Groq API 키: 서버에 파일로 두거나, 나중에 환경변수로 넘기도록 변경할 수 있습니다.

필요하면 `-e` 로 오버라이드:

```bash
sudo docker run -d \
  -p 8080:8080 \
  --name bible-api \
  -e cors.allowed-origins=http://공인IP,http://공인IP:80 \
  bible-api
```

### 4-2. 단순 실행 (기본 설정 그대로 사용)

```bash
sudo docker run -d -p 8080:8080 --name bible-api bible-api
```

### 4-3. 동작 확인

```bash
curl -s http://localhost:8080/api/bible/chapters?book=1
```

`{"maxChapter":50}` 같은 JSON이 나오면 정상입니다.

---

## 5. 프론트엔드 실행 (Oracle Cloud ARM 서버)

```bash
sudo docker run -d -p 80:80 --name bible-react bible-react
```

브라우저에서 `http://공인IP` 로 접속해 보면 됩니다.

---

## 6. CORS 설정 (백엔드)

프론트 도메인(또는 IP)이 백엔드와 다르면 CORS 허용이 필요합니다.  
서버에서 `application.properties` 를 쓰는 경우, **실행 전**에 다음을 추가하거나 수정합니다.

```properties
# 프론트 접속 주소 (쉼표로 여러 개 가능)
cors.allowed-origins=http://공인IP,http://공인IP:80,http://localhost
```

이미지를 다시 빌드해야 하면, `application.properties` 수정 후 WAR 재빌드 → `docker build` → `docker run` 순서로 진행하면 됩니다.  
또는 위 4-1처럼 `-e cors.allowed-origins=...` 로 컨테이너 실행 시 넘겨도 됩니다 (Spring이 환경변수로 읽도록 되어 있을 때).

---

## 7. Oracle Cloud 보안 목록(방화벽) 열기

1. Oracle Cloud 콘솔 → **네트워킹** → **가상 클라우드 네트워크** → 사용 중인 VCN 선택  
2. **서브넷** → 인스턴스가 있는 서브넷 선택  
3. **보안 목록** → 해당 서브넷에 연결된 기본 보안 목록 클릭  
4. **인바운드 규칙** → **규칙 추가**  
   - **소스 CIDR**: `0.0.0.0/0` (전체 허용) 또는 필요한 IP만  
   - **대상 포트 범위**: `80` (프론트), `8080` (백엔드 API)  
   - **설명**: 예) HTTP, API  

저장 후, 브라우저에서 `http://공인IP` (80), `http://공인IP:8080/api/...` (8080) 로 접속이 되어야 합니다.

---

## 8. 한 번에 실행 (docker compose 예시)

서버에 백엔드·프론트 폴더가 있고, 같은 디렉터리에 `docker-compose.yml` 을 두고 실행할 수 있습니다.

`~/bible-app/docker-compose.yml` 예시:

```yaml
services:
  bible-api:
    image: bible-api
    build: ../Bible-RESTful-API
    ports:
      - "8080:8080"
    environment:
      - cors.allowed-origins=http://공인IP,http://공인IP:80

  bible-react:
    image: bible-react
    build:
      context: ../Bible-React
      args:
        VITE_API_BASE_URL: http://공인IP:8080
    ports:
      - "80:80"
    depends_on:
      - bible-api
```

실행:

```bash
cd ~/bible-app
sudo docker compose up -d
```

이 경우 `build` 로 이미지가 없으면 자동으로 빌드합니다.  
백엔드는 WAR가 있어야 하므로, `../Bible-RESTful-API` 에서 먼저 `./gradlew war` 를 한 뒤 `docker compose up` 하는 것이 좋습니다.

---

## 9. 자주 쓰는 명령어 정리

| 작업 | 명령어 |
|------|--------|
| 백엔드 로그 보기 | `sudo docker logs -f bible-api` |
| 프론트 로그 보기 | `sudo docker logs -f bible-react` |
| 백엔드 중지 | `sudo docker stop bible-api` |
| 프론트 중지 | `sudo docker stop bible-react` |
| 백엔드 다시 시작 | `sudo docker start bible-api` |
| 프론트 다시 시작 | `sudo docker start bible-react` |
| 컨테이너 삭제 후 재실행 | `sudo docker rm -f bible-api bible-react` 후 위 `docker run` 다시 실행 |

---

## 10. 트러블슈팅

- **80/8080 접속 안 됨**  
  → Oracle 보안 목록 인바운드 규칙, 서버 내부 방화벽(`sudo ufw status`) 확인.

- **프론트에서 API 호출 실패 (CORS)**  
  → 백엔드 `cors.allowed-origins` 에 `http://공인IP`, `http://공인IP:80` 포함 여부 확인.

- **API 호출 404**  
  → 백엔드가 8080에서 떠 있는지 `curl http://localhost:8080/api/bible/chapters?book=1` 로 확인.  
  → 프론트 빌드 시 `VITE_API_BASE_URL` 이 `http://공인IP:8080` (또는 사용하는 도메인)인지 확인.

- **DB 연결 실패**  
  → DB가 Oracle Cloud VCN 내부에 있으면 보안 목록/NSG에서 3306 등 DB 포트 허용.  
  → `application.properties` 의 DB URL이 ARM 서버에서 접근 가능한 IP/호스트인지 확인.

이 가이드대로 진행하면 Oracle Cloud ARM 서버에 Docker를 설치하고, Bible 백엔드·프론트엔드를 각각 Docker로 실행할 수 있습니다.
