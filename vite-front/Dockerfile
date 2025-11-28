# ========== Stage 1: 빌더 (Build Stage - React 빌드) ==========
# Node 이미지를 사용하여 React 빌드를 수행합니다.
FROM node:22.13.1 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 파일만 복사하여 종속성 설치 (캐싱 활용)
# COPY [소스] [목적지/]
COPY vite-front/package*.json ./
RUN npm install

# 모든 소스 파일을 복사
COPY . .

# React 프로젝트 빌드 실행
# 빌드 결과는 일반적으로 /app/build 또는 /app/dist에 생성되나, 
# 사용자가 /app/build로 지정했으므로 그에 맞춥니다.
RUN npm run build 

# ========== Stage 2: 런타임 (Runtime Stage - Nginx 서빙) ==========
# 경량 Nginx 이미지를 사용하여 정적 파일을 서빙합니다.
FROM nginx:alpine

# 1. 빌드 스테이지에서 생성된 정적 파일(build 폴더)을 Nginx 웹 루트로 복사
COPY --from=build /app/build /usr/share/nginx/html

# 2. 사용자 정의 Nginx 설정 파일 복사
# 프로젝트 루트(./vite-front) 기준: nginx/nginx.conf 경로
COPY vite-front/nginx/nginx.conf /etc/nginx/nginx.conf

# 웹 기본 포트 노출
EXPOSE 80

# Nginx 시작 명령어 (데몬 off로 포그라운드 실행)
CMD ["nginx", "-g", "daemon off;"]