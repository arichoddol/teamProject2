# 같이달리조
러닝 용품 판매 및 크루 활동 플랫폼

## 🏃‍♂️ 프로젝트 소개 - 러닝 커뮤니티 & 쇼핑몰 플랫폼
본 프로젝트는 러닝 커뮤니티(크루)와 러닝 용품 쇼핑몰 기능을 하나의 서비스로 제공하는 풀스택 기반 팀 프로젝트입니다.

사용자는 크루 생성·크루 게시판 활동·실시간 채팅·챗봇·러닝 스케줄 관리 등을 이용할 수 있으며,
동시에 상품 구매·장바구니·결제·게시판 등 쇼핑몰 기능도 함께 사용할 수 있습니다.

#### 프로젝트 기간
2025.05 - 2025.12

#### 팀원 소개

|박한나|정기호|이승준|유연준|허린|천승우
|-----|-----|-----|-----|-----|-----|
|팀장|팀원|팀원|팀원|팀원|팀원
|-----|-----|-----|-----|-----|-----|
|JWT<br>Spring Security<br>Member<br>OAuth2|게시판CRUD<br>상품CRUD<br>CICD<br>라우터 설계<br>Git관리<br>Header|관리자CRUD<br>관리자 대시보드<br>권한 기반 기능 제한|크루CRUD<br>크루 게시판<br>크루 채팅|DB 설계<br>크루 가입<br>크루 메인<br>크루 챗봇<br>크루 회원<br>크루 일정|장바구니<br>결제<br>Open API|



---

## 📁 기술 스택

#### Backend
- Java 17 / Spring Boot 3.xx
- WebSocket(STOMP), AWS S3
- JPA, MySQL
- Spring Security, JWT

#### Frontend
- React, Redux Toolkit
- Vite, Axios, STOMP

#### DevOps
- AWS EC2, RDS, S3
- GitHub Actions + EC2 + Docker + Nginx (CI/CD)

---

### 🧩 시스템 구조
- React -> Spring Boot(API / WebSocket) -> MySQL(RDS)
- 이미지 : AWS S3 저장
- WebSocket(STOMP) 실시간 통신
  
---

### 🚀 배포

#### CI/CD & Server 구조

- GitHub Actions로 자동 빌드/배포  
- Docker로 컨테이너 구성  
- Nginx Reverse Proxy
- EC2에 애플리케이션 실행

---

## ⭐ 전체 주요 기능

<details>
<summary>👥 1. 회원/인증</summary>
  
- 회원가입 / 로그인 / 로그아웃
- JWT 기반 인증
- Spring Security 기반 권한 관리
- 멤버 (회원 정보 수정, 프로필) (CRUD)
</details>

<details>
<summary>🛒 2. 쇼핑몰 기능</summary>
  
- 러닝 용품 상품 등록 / 수정 / 삭제 (CRUD)
- 상품 목록 및 상세 조회
- 쇼핑몰 게시판
- 장바구니/결제 기능  
</details>

<details>  
<summary>🏃‍♂️ 3. 크루(Crew) 기능</summary>
  
- 크루 생성 / 조회 / 수정 / 삭제 (CRUD)
- 크루 목록 및 상세 조회
- 크루 멤버 관리(가입 요청 / 승인 / 거절)
- 크루 챗봇 (일정 FAQ)
- 크루 전용 게시판
- 크루 전용 실시간 채팅(WebSocket + STOMP + SockJS)
- 크루 러닝 스케쥴 (일정 등록/조회)
</details>

<details>
<summary>🛠 4. 관리자(Admin)</summary>
  
- 전체 사용자 관리
- 크루 생성 승인
- 쇼핑몰 상품 관리
- 게시판 / 공지사항 관리
- 관리자 대시보드  
</details>


<details>
<summary>🌐 5. Open API</summary>
  
- 공공 데이터 기반 마라톤 대회 일정 API
- 날씨 API
</details>


<details>
<summary>🔹 6. 공통 기능</summary>
  
- JWT 기반 인증 시스템
- Spring Security 권환 관리
- 이미지 업로드(S3)
- 페이징 처리
- 검색 기능
- CI/CD 자동 배포(GitHub Actions + Docker + EC2 + Nginx)  
</details>

---

# 🧑‍💻 담당한 기능
### 🏃‍♂️ 1. 크루 CRUD
- 크루 생성 / 수정 / 삭제
- 크루 리스트 조회
- 크루 상세 조회

### 📰 2. 크루 게시판 CRUD
- 게시글 작성 / 수정 / 삭제 / 조회
- 이미지 업로드(S3) 처리
- 페이징 + 검색 기능

### 💬 3. 실시간 크루 채팅
- SockJS + STOMP 클라이언트 연결
- WebSocket 서버 엔드포인트 구성
- 채팅 메시지 브로드캐스팅
- 입장 / 퇴장 이벤트 처리
- 프로필 변경 시 채팅창 반영

### ⚙️ 기타
- 프론트/백엔드 연동
- Axios JWT 인증 포함 처리
