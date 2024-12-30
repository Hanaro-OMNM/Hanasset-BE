<img width="1025" alt="image" src="https://github.com/user-attachments/assets/fb7714ac-960e-458c-90ac-a2c4f52a96d2" />

# 전월세 대출 맞춤 추천 및 상담 지원 플랫폼 

##### Hanasset Backend Repository

지도 위에서 부동산 매물 확인과 대출 상담까지 한번에


## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [팀원 구성](#팀원-구성)
3. [개발 기간](#개발-기간)
4. [기술 스택](#기술-스택)
5. [개발 환경](#개발-환경)
6. [ERD](#ERD)
7. [API 명세](#API-명세)
8. [데이터 수집](#데이터-수집)
9. [시스템 아키텍처](#시스템-아키텍처)
10. [기능 소개](#기능-소개)
   - [전월세 매물 조회](#전월세-매물-조회)
   - [맞춤형 대출 상품 추천](#맞춤형-대출-상품-추천)
   - [실시간 상담 서비스](#실시간-상담-서비스)
11. [문의](#문의)


---

## 프로젝트-개요
HANA + ASSET은 사용자 자산 정보를 바탕으로 서울시 전월세 매물을 분석하여 적합한 대출 상품을 추천하고,  
하나은행의 전문 대출 상담사와 실시간 상담을 통해 전월세 자금 대출 계획을 효율적으로 수립할 수 있는 플랫폼입니다.

---

## 팀원 구성

<div align="center">

| **김미강** | **양지은👑** | **이동윤** | **이인수** | **최선정** | **한성민** |
| :------: |  :------: | :------: | :------: | :------: | :------: |
| [<img src="https://avatars.githubusercontent.com/u/113813881?v=4" height=150 width=150> <br/> @mkngkm](https://github.com/mkngkm) | [<img src="https://avatars.githubusercontent.com/u/38836598?v=4" height=150 width=150> <br/> @yje9802](https://github.com/yje9802) | [<img src="https://avatars.githubusercontent.com/u/30024566?v=4" height=150 width=150> <br/> @leedy903](https://github.com/leedy903)| [<img src="https://avatars.githubusercontent.com/u/77774140?v=4" height=150 width=150> <br/> @insoo00](https://github.com/insoo00)| [<img src="https://avatars.githubusercontent.com/u/128480236?v=4" height=150 width=150> <br/> @Choeseonjeong](https://github.com/Choeseonjeong)| [<img src="https://avatars.githubusercontent.com/u/115688628?v=4" height=150 width=150> <br/> @kkx7787](https://github.com/kkx7787)

</div>

<br>

--- 

## 개발 기간

- 2024년 10월 17일 ~ 2024년 10월 30일: 프론트 엔드
- 2024년 12월 16일 ~ 2024년 12월 27일 : 백엔드
- 배포: 2024년 12월 29일
- 최종 발표 및 평가: 2024년 12월 30일

---

## 기술 스택 
| **분류**       | **스택**                                                                                   |
|----------------|-------------------------------------------------------------------------------------------|
| **Language**   | ![Java](https://img.shields.io/badge/Java-17-007396?style=flat&logo=openjdk&logoColor=white) ![TypeScript](https://img.shields.io/badge/TypeScript-4.5-3178C6?style=flat&logo=typescript&logoColor=white) |
| **Framework**  | ![SpringBoot](https://img.shields.io/badge/SpringBoot-3.1.1-6DB33F?style=flat&logo=springboot&logoColor=white) ![React](https://img.shields.io/badge/React-18.3.1-61DAFB?style=flat&logo=react&logoColor=black) |
| **Build**      | ![Gradle](https://img.shields.io/badge/Gradle-7.0-02303A?style=flat&logo=gradle&logoColor=white) ![Vite](https://img.shields.io/badge/Vite-4.0-646CFF?style=flat&logo=vite&logoColor=white) |
| **Front-end**  | ![TailwindCSS](https://img.shields.io/badge/TailwindCSS-3.2-06B6D4?style=flat&logo=tailwindcss&logoColor=white) ![Recoil](https://img.shields.io/badge/Recoil-Experimental-3578E5?style=flat&logoColor=white) ![Axios](https://img.shields.io/badge/Axios-0.21.1-5A29E4?style=flat) ![React Naver Maps](https://img.shields.io/badge/React%20Naver%20Maps-API-61DAFB?style=flat&logo=react&logoColor=black) ![React Query](https://img.shields.io/badge/React%20Query-3.39.0-FF4154?style=flat&logo=reactquery&logoColor=white) |
| **Back-end**   | ![Spring Security](https://img.shields.io/badge/Spring%20Security-5.6.1-6DB33F?style=flat&logo=springsecurity&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-2.5.6-6DB33F?style=flat&logo=spring&logoColor=white) ![OAuth 2.0](https://img.shields.io/badge/OAuth%202.0-Standard-3C7EBB?style=flat&logo=oauth&logoColor=white) ![WebSocket](https://img.shields.io/badge/WebSocket-API-4A90E2?style=flat) ![STOMP](https://img.shields.io/badge/STOMP-Protocol-800000?style=flat) ![Spring Batch](https://img.shields.io/badge/Spring%20Batch-4.3.6-6DB33F?style=flat&logo=spring&logoColor=white) ![Spring Mail](https://img.shields.io/badge/Spring%20Mail-3.0.0-6DB33F?style=flat) ![ElasticSearch](https://img.shields.io/badge/ElasticSearch-8.5.0-005571?style=flat&logo=elasticsearch&logoColor=white) |
| **Data**       | ![Python](https://img.shields.io/badge/Python-3.10-3776AB?style=flat&logo=python&logoColor=white) ![BeautifulSoup4](https://img.shields.io/badge/BeautifulSoup4-WebScraping-4B8BBE?style=flat) |
| **Database**   | ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-6.2-DC382D?style=flat&logo=redis&logoColor=white) |
| **Tool**       | ![Postman](https://img.shields.io/badge/Postman-API%20Testing-FF6C37?style=flat&logo=postman&logoColor=white) ![IntelliJ](https://img.shields.io/badge/IntelliJ%20IDEA-2023-000000?style=flat&logo=intellijidea&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-Design-FF7262?style=flat&logo=figma&logoColor=white) ![VSCode](https://img.shields.io/badge/VSCode-1.77-007ACC?style=flat&logo=visualstudiocode&logoColor=white) ![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-85EA2D?style=flat&logo=swagger&logoColor=white) ![Github](https://img.shields.io/badge/Github-Code%20Hosting-181717?style=flat&logo=github&logoColor=white) |
| **Deploy**     | ![EC2](https://img.shields.io/badge/AWS%20EC2-Cloud-orange?style=flat&logo=amazonaws&logoColor=white) ![AWS RDS](https://img.shields.io/badge/AWS%20RDS-Database-527FFF?style=flat&logo=amazonrds&logoColor=white) ![Jenkins](https://img.shields.io/badge/Jenkins-CI/CD-D24939?style=flat&logo=jenkins&logoColor=white) |
| **Communication** | ![Notion](https://img.shields.io/badge/Notion-Wiki-000000?style=flat&logo=notion&logoColor=white) ![Slack](https://img.shields.io/badge/Slack-Chat-4A154B?style=flat&logo=slack&logoColor=white) ![Jira](https://img.shields.io/badge/Jira-Project%20Management-0052CC?style=flat&logo=jira&logoColor=white) |


## 개발 환경


![image](https://github.com/user-attachments/assets/9cdb692d-52d3-4f64-bd87-33e4bd0d98d9)


## ERD


![image](https://github.com/user-attachments/assets/8ecc4335-74a0-4a36-9a00-cdba9a6f59d1)


## API 명세


![image](https://github.com/user-attachments/assets/cb41321f-96ff-453f-b10a-b36145f8d347)


## 데이터 수집


![image](https://github.com/user-attachments/assets/29e3653e-41f8-4711-8a8e-5c95e2d97d6e)


## 시스템 아키텍처


![image](https://github.com/user-attachments/assets/e62f8a48-653e-4040-b585-b540c559a435)




## 기능 소개 
<br>

### 전월세 매물 조회
- 🏠 **간편하게 부동산 매물 정보 확인**  
  - 최신 전월세 매물 정보 제공  
  - 원하는 지역의 모든 매물 위치를 한 눈에 파악 
  - 매물 상세 정보 열람
    

  <img src="https://github.com/user-attachments/assets/c1a942a7-ef62-476e-8300-47a0dc19bfba" alt="Image 1" width="800"/>
  <br>
  <img src="https://github.com/user-attachments/assets/ddc13082-62e1-41ef-bf29-36ca3e4d8980" alt="Image 2" width="800"/>
    





---

### 맞춤형 대출 상품 추천
- 💳 **사용자 자산 기반 대출 상품 추천**  
  - 연소득, 대출 현황 등의 자산 정보를 분석하여 최적의 상품 제안
  - 대출 조건 비교 기능
 <img src="https://github.com/user-attachments/assets/dd0314a2-652c-4b6f-94c2-df233946ea9b" alt="Image 2-1" width="800"/>
  <br>
<img src="https://github.com/user-attachments/assets/174a81f7-4c5d-4e44-9049-7791d99ecef9" alt="Image 2-2" width="800"/>

<br>
---

### 실시간 상담 서비스
- 💬 **대출 상담 및 계획 수립 지원**  
  - 하나은행 전문 상담사와 채팅 기능  
  - 대출 계획 수립 및 맞춤형 컨설팅 제공 
  - 상담 내역 저장 및 다시보기 기능
  - 예약을 통해 원하는 시간에 상담 가능 
<img src="https://github.com/user-attachments/assets/332b94cb-544e-4afa-a54a-68eae1200a01" alt="Image 3" width="800"/>
  
