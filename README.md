
<div align="center">

## [OOTC 바로가기](https://ootc.life)

![Group 18](https://github.com/user-attachments/assets/5032a998-758f-4826-b219-5712f1351da8)

## Phase 1 비회원 + 회원 공통 기능

![Group 25](https://github.com/user-attachments/assets/6bbf9868-3793-4fdf-b0de-1f651e7e9f55)

</div>

## ✅ Tech

### ✔️ back-end
![Group 57](https://github.com/user-attachments/assets/416add11-3e3b-4786-b35a-5f5b0cc00ff6)

### ✔️ infra
![Group 58](https://github.com/user-attachments/assets/e7ac81d5-9697-4b3e-948b-db758bfc7c13)

## ✅ Infrastructure

### ✔️ back-end
![Group 27](https://github.com/user-attachments/assets/c3f6c5f2-a96c-41fe-afb2-1b7e91f92c43)

## ✅ CI/CD Flow

### ✔️ back-end
![Group 26](https://github.com/user-attachments/assets/adeb544b-84fb-479f-873b-6ebcb1533e9d)

#### Script
```yml
name: Java CI/CD with Gradle

on:
  pull_request:
    branches: [ main, develop ]

permissions:
  contents: read

env:
  DOCKERHUB_USERNAME: ${{ secrets.DOCKERHUB_USERNAME }}

jobs:
  build:
    runs-on: ubuntu-22.04
    permissions:
      pull-requests: write

    steps:
      - name: 레포지토리 체크아웃
        uses: actions/checkout@v4
        with:
          token: ${{ secrets.SUBMODULE_TOKEN }}
          submodules: true

      - name: JDK 17 설치
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: gradlew 권한 부여
        run: chmod +x gradlew

      - name: Gradle 통해 빌드
        run: ./gradlew clean build

      - name: DockerHub 로그인
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}

      - name: Docker 이미지 이름 및 환경 설정
        run: |
          if [[ "${{ github.event.pull_request.base.ref }}" == "main" ]]; then
            echo "IMAGE_NAME=fashion-forecast-prod" >> $GITHUB_ENV
          elif [[ "${{ github.event.pull_request.base.ref }}" == "develop" ]]; then
            echo "IMAGE_NAME=fashion-forecast-dev" >> $GITHUB_ENV
          fi

      - name: 도커 빌드 & 푸시
        uses: docker/build-push-action@v6
        with:
          context: .
          file: ./Dockerfile
          push: true
          platforms: linux/amd64
          tags: ${{ secrets.DOCKERHUB_USERNAME }}/${{ env.IMAGE_NAME }}:latest

  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - name: 배포 타겟 설정
        run: |
          if [[ "${{ github.event.pull_request.base.ref }}" == "main" ]]; then
            echo "DEPLOY_ENV=prod" >> $GITHUB_ENV
          elif [[ "${{ github.event.pull_request.base.ref }}" == "develop" ]]; then
            echo "DEPLOY_ENV=dev" >> $GITHUB_ENV
          fi
      - name: EC2 원격 접속 및 Docker compose
        uses: appleboy/ssh-action@master
        with:
          username: ${{ secrets.EC2_DEV_USERNAME }}
          host: ${{ secrets.EC2_DEV_HOST }}
          key: ${{ secrets.EC2_DEV_PRIVATE_KEY }}
          script_stop: true
          script: |
            sudo docker pull ${{ env.DOCKERHUB_USERNAME }}/fashion-forecast-${{ env.DEPLOY_ENV }}:latest
            sudo docker compose -f docker-compose-${{ env.DEPLOY_ENV }}.yml down
            sudo docker compose -f docker-compose-${{ env.DEPLOY_ENV }}.yml up -d
            sudo docker image prune -f

```

## ✅ Works

### ✔️ Phase 1

#### ✨김현재 (front-end)

#### 🍎 김덕빈 (front-end)

#### 🍒 최이주 (back-end)

#### 🤖 박형균 (back-end)
| No | Work               | Description                                                                                                  |
|----|--------------------|--------------------------------------------------------------------------------------------------------------|
| 1  | 날씨 조회 API          | 기상청 단기예보 API를 활용하여 위도, 경도, 시간을 기준으로 날씨 조회                                                                    |
| 2  | 최근 검색어 API         | Redis를 이용한 사용자 최근 검색어 조회, 삭제                                                                                 |
| 3  | 게스트 로그인 API        | 비회원을 위한 uuid 발급                                                                                              |
| 4  | 날씨 데이터 스케쥴러 작업     | 매일 새벽 5시 과거 날씨 데이터 삭제 스케쥴러, QueryDsl를 이용한 벌크 연산                                                              |
| 5  | 날씨 제공 지역 초기화 작업    | 애플리케이션 실행 시 날씨 제공 지역 csv파일을 읽어 DB에 저장, JdbcTemplate를 이용한 벌크 연산                                               |
| 6  | 백엔드 도커라이징 작업       | docker-compose를 이용한 애플리케이션 도커라이징                                                                             |
| 7  | 이니셜 데이터 마이그레이션     | Flyway를 이용한 이니셜 데이터 마이그레이션                                                                                   |
| 8  | github 협업 환경 세팅    | PR Template, Issue Template 작성                                                                               |
| 9  | restDoc 적용         | 테스트 코드 작성 강제를 위한 RestDocs 적용, adocsTemplate 작성                                                               |
| 10 | AWS 개발 및 배포 환경 구축  | EC2, RDS를 사용하여 dev & prod 환경 구축                                                                              |
| 11 | 백엔드 CI/CD 파이프라인 구축 | CI/CD 스크립트 작성 및 github actions를 이용한 자동화                                                                      |
| 12 | 백엔드 배포 작업          | certbot 및 Let's Encrypt 를 사용하여 백엔드 도메인 https 인증서 발급,  nginx를 이용하여 백엔드 도메인에 접근 시 스프링부트 애플리케이션으로 reverse proxy |


### ✔️ Phase 2

#### ✨김현재 (front-end)

#### 🍒 최이주 (back-end)

#### 🤖 박형균 (back-end)
| No | Work       | Description                                                               |
|----|------------|---------------------------------------------------------------------------|
| 1  | 소셜 로그인 API | Oauth2를 이용한 카카오, 구글 소셜 로그인 구현, 로그인 성공 시 리프레시 토큰(Cookie) 및 액세스 토큰(body) 발급 |

### PM
| <img alt="스크린샷 2024-10-02 오후 9 32 55" src="https://github.com/user-attachments/assets/8ce52231-5083-480d-b1ee-0d7e15ed1c01" width="130" height="130"> |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                                                              조현아                                                                              |

### UX/UI
| <img alt="stuff-xp" src="https://github.com/user-attachments/assets/05c10544-1971-48b3-bdd9-740e09a805d0" width="130" height="130"> |
|:-----------------------------------------------------------------------------------------------------------------------------------:|
|                                                                 최태식                                                                 |

### front-end

| <img src="https://avatars.githubusercontent.com/u/115006670?v=4" width="130" height="130"> | <img src ="https://avatars.githubusercontent.com/u/162319857?v=4" width="130" height="130"> |
|:------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
|                            [김현재](https://github.com/presentKey)                            |                              [김덕빈](https://github.com/Db0111)                               |

### back-end

| <img src="https://avatars.githubusercontent.com/u/122284322?v=4" width="130" height="130"> | <img src="https://avatars.githubusercontent.com/u/143402486?v=4" width="130" height="130"> |
|:------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------:|
|                           [박형균 [팀장]](https://github.com/phk1128)                           |                           [최이주](https://github.com/cherryiJuice)                           |


---
### 브랜치 네이밍 규칙
브랜치 종류/이슈넘버-기능이름

e.g) feature/#5-login

### 브랜치 전략
gitflow 전략을 따르지만, release 브랜치가 없는 형태 

main : 최종 배포 브랜치

develop : 개발 단계 브랜치 (디폴트)

### 커밋 메세지 컨벤션

| 타입       | 설명                      |
|----------|-------------------------|
| feat     | 새로운 기능을 추가              |
| fix      | 버그를 수정                  |
| docs     | 문서와 관련된 변경 사항을 기록       |
| style    | 코드 포맷팅                  |
| refactor | 리팩토링 작업을 기록             |
| test     | 테스트 코드를 추가하거나 수정        |
| chore    | 초기세팅 및 코드에 영향을 주지 않는 작업 |
