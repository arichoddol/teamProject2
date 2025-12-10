# 🏃‍♂️ 같이달리조
러닝 커뮤니티 & 쇼핑몰 플랫폼

## 🏃‍♂️ 프로젝트 소개
본 프로젝트는 러닝 크루 활동과 러닝 용품 쇼핑몰을 하나의 서비스에 결합하여 제공하는 통합 플랫폼의 풀스택 기반 팀 프로젝트입니다.

사용자는 크루 생성·게시판·실시간 채팅·일정 관리·챗봇 등의 커뮤니티 기능과
상품·장바구니·결제 등 쇼핑몰 기능을 한 곳에서 이용할 수 있습니다.

**📅프로젝트 기간** : 2025.05 - 2025.12
**👥 팀 인원** : 6명 

---

## 팀 구성 및 역할

<table>
  <tr>
    <th>박한나(팀장)</th>
    <th>정기호</th>
    <th>이승준</th>
    <th><strong>유연준(본인)</strong></th>
    <th>허린</th>
    <th>천승우</th>
  </tr>
  <tr>
    <td>JWT<br>Spring Security<br>Member<br>OAuth2</td>
    <td>게시판 CRUD<br>상품 CRUD<br>CI/CD<br>라우터 설계<br>Git 관리<br>Header</td>
    <td>관리자 CRUD<br>관리자 대시보드<br>권한 기반 기능 제한</td>
    <td><strong>크루 CRUD<br>크루 게시판<br>크루 채팅</strong></td>
    <td>DB 설계<br>크루 가입<br>크루 메인<br>크루 챗봇<br>크루 회원<br>크루 일정</td>
    <td>장바구니<br>결제<br>Open API</td>
  </tr>
</table>

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
- Spring Security 권한 관리
- 이미지 업로드(S3)
- 페이징 / 검색
- CI/CD 자동 배포(GitHub Actions + Docker + EC2 + Nginx)  
</details>

---

## 🧑‍💻 담당한 기능

### 🏃‍♂️ 1. 크루 CRUD
<details>
<summary>- 크루 생성 / 수정 / 삭제</summary>
<img width="297" height="433" alt="image" src="https://github.com/user-attachments/assets/dd19b151-1e27-4265-aa21-5190d3f21e59" />
<img width="346.4" height="308.4" alt="image" src="https://github.com/user-attachments/assets/6c7cea0c-de9a-4ed5-baa8-012c0a18bac9" />

</details>

<details>
<summary>- 크루 목록 조회</summary>
<img width="232" height="406" alt="image" src="https://github.com/user-attachments/assets/5aafba4f-9812-46d2-811c-35179796014a" />
<img width="354.5" height="420.5" alt="image" src="https://github.com/user-attachments/assets/2fc03f8e-ae17-4eaf-8f3e-a9e5fbe270f6" />

  
</details>

<details>  
<summary>- 크루 상세 조회</summary>
<img width="478.5" height="282.5" alt="image" src="https://github.com/user-attachments/assets/31d920c0-5653-4eae-b9d8-1573ccb67646" />  
</details>


---

### 📰 2. 크루 게시판 CRUD
<details>
  <summary>- 게시글 작성 / 수정 / 삭제 / 조회</summary>
<img width="509.5" height="313.5" alt="image" src="https://github.com/user-attachments/assets/074e5511-f38e-41fd-a629-c4080931341d" />
<img width="445" height="241.5" alt="image" src="https://github.com/user-attachments/assets/cd6fd4fb-5116-4c6e-a983-2cf27f5b0545" />
<img width="278.5" height="428" alt="image" src="https://github.com/user-attachments/assets/cf8a400d-5f66-4a37-b096-b049ab4e6030" />
<img width="278.5" height="428" alt="image" src="https://github.com/user-attachments/assets/8908682c-3d07-4ea8-bfd4-74240ee99bff" />  
</details>
    
<details>
  <summary>- 게시글 목록</summary>
  <img width="578.5" height="285" alt="image" src="https://github.com/user-attachments/assets/a89c234a-b604-4b58-9497-92e52d14652a" />
</details>
- 이미지 업로드(S3) 처리
- 페이징 + 검색 기능

### 💬 3. 실시간 크루 채팅
- WebSocket 서버 엔드포인트 구성
<details>
<summary>- SockJS + STOMP 클라이언트 연결</summary>
<img width="261" height="396.5" alt="image" src="https://github.com/user-attachments/assets/d8d358ed-3e92-4920-8d06-24aebaead2b4" />
<img width="259.7" height="102.9" alt="image" src="https://github.com/user-attachments/assets/39592300-0eea-4bd6-ae05-b918eb41d1df" />  
</details>

<details>
<summary>- 채팅 메시지 브로드캐스팅</summary>
<img width="486.5" height="361.5" alt="image" src="https://github.com/user-attachments/assets/9d271401-169c-4d6a-89d8-85024852cb73" />  
</details>

<details>
<summary>- 입장 / 퇴장 이벤트 처리</summary>
  <img width="371" height="147" alt="image" src="https://github.com/user-attachments/assets/886ad4e8-19f7-43a8-b49e-50ffbd0b5a47" />
  <img width="366.5" height="30.5" alt="image" src="https://github.com/user-attachments/assets/43bf6d35-8489-46ce-a646-dbd9c3dcd608" />
</details>

- 프로필 변경 시 채팅창 반영

### ⚙️ 기타
- 프론트/백엔드 연동
- Axios JWT 인증 포함 처리
