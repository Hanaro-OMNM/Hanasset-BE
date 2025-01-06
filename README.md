<div align="center"><img width="825" alt="image" src="https://github.com/user-attachments/assets/83552c73-1281-4ee5-b7ac-f9fcaba65de8" /></div>

<br>
<br>
<br>

<div align="center"><h1>🏠 <span style="color: #0B9B97;">Hanasset</span> - 전월세 대출 맞춤 추천 및 상담 지원 플랫폼 </h1></div>

<span style="color: #0B9B97; font-weight: bold;">"지도 위에서 부동산 매물 확인과 대출 상담까지 한번에"</span> 라는 컨셉 아래, 손님의 기본적인 자산 정보를 기반으로 하나은행의 전월세 대출 상품 안내 및 채팅 상담까지 지원하는 플랫폼입니다.

<br>

## 목차
- [목차](#목차)
- [프로젝트 소개](#프로젝트-소개)
  - [💡 프로젝트를 왜 시작하게 되었나요?](#-프로젝트를-왜-시작하게-되었나요)
  - [🔑 프로젝트를 핵심은 무엇인가요?](#-프로젝트를-핵심은-무엇인가요)
  - [🎁 프로젝트가 가져올 수 있는 기대 효과는 무엇인가요?](#-프로젝트가-가져올-수-있는-기대-효과는-무엇인가요)
- [팀 소개](#팀-소개)
- [개발 기간](#개발-기간)
- [기술 스택](#기술-스택)
- [ERD](#erd)
- [API 명세](#api-명세)
- [시스템 아키텍처](#시스템-아키텍처)
  - [인프라 아키텍처](#인프라-아키텍처)
  - [FE-BE 아키텍처](#fe-be-아키텍처)
- [핵심 기능 소개](#핵심-기능-소개)
  - [🏠 전월세 매물 조회](#-전월세-매물-조회)
  - [💳 맞춤형 대출 상품 추천](#-맞춤형-대출-상품-추천)
  - [💬 실시간 상담 서비스](#-실시간-상담-서비스)


---

<br>

## 프로젝트 소개
Hanasset(하나셋)은 서울시의 모든 전월세 매물을 지도 상에서 확인할 수 있고, 

손님 자산 기반 맞춤형 대출 상품 목록 확인과 하나은행 전문 대출 상담사와의 채팅을 통한 

전월세 자금 대출 계획 수립을 도와주는 전월세 대출 상담 플랫폼입니다.

<br>

### 💡 프로젝트를 왜 시작하게 되었나요?

<div align="center" style="margin-bottom: 20px;">
    <img width="800" alt="손님_니즈" src="https://github.com/user-attachments/assets/f11ad6fb-a6ec-45eb-a67c-8b2e7ea2681d" />
</div>

서울에서 전세나 월세 계약을 위해서는 이제는 대출이 거의 필수라 할 수 있는데요, 전월세 자금 대출을 위해서는 부동산 가계약이 선행되어야 합니다.
또한, 대출 가능 여부가 매물에 따라 결정되는 경우도 많다고 합니다.

그렇지만 대출이 익숙하지 않은 청년층에게는 어떤 대출 상품이 좋은지, 아니 어디서 부터 시작해야 할 지 계획을 세우는 것부터가 막막할 것입니다.

직방이나 다방 처럼 부동산 매물을 검색할 수 있는 부동산 전문 플랫폼은 많지만 이렇게 특정 매물에 대해 가입 가능한 대출 상품까지 안내해주는 플랫폼은 없기 때문에 이렇게  Hanasset 프로젝트를 기획하게 되었습니다.

<br>

### 🔑 프로젝트를 핵심은 무엇인가요?
서울시에서 전월세 부동산 계약을 하고 싶으면서! 전월세 보증금 대출을 계획하고 있는! 대한민국의 모든 미혼에 자녀가 없는 만 34세 이하의 청년에게 가계약이 없이도, 대출에 대한 이해 없이도, 멎춤형 대출 계획을 세울 수 있는 방향성 제시해주는 것입니다!

<br>

### 🎁 프로젝트가 가져올 수 있는 기대 효과는 무엇인가요?
- **손님 측면**
  - 지도를 둘러보며 간편하게 부동산 매물 정보를 확인할 수 있습니다.
  - 상담 예약을 통해 시간 절약 및 온라인을 통한 효율이고 부담 없는 대출 상담이 가능합니다.
  - 관심있는 매물에 적합한 대출 상품 추천 및 비교를 통해 보다 효율적인 대출 계획을 수립할 수 있습니다.
- **하나은행 측면**
  - 상담을 통해 청년이 선호하는 매물과 대출 상품을 분석하여 유의미한 대출 수요 데이터를 확보할 수 있습니다.
  - 대출 수요 데이터를 기존의 대출 상품 개선 및 신규 대출 상품 설계에 활용할 수 있습니다.
  - 손님과의 접점 확대를 통한 하나은행이라는 브랜드의 신뢰도 상승을 기대할 수 있습니다.

<br>

## 팀 소개

<div align="center">
    <h3> 👋 OMNM(오늘도 만나고! 내일도 만나고!) 👋 </h3>
    <div>오만내만은</div>
    <div>개성 가득한 팀원들이</div>
    <div>오늘도 만나고, 내일도 만나고, 매일매일 만나며 프로젝트를 디벨롭 하고자 하는 열정이 담긴 팀명입니다!</div>
</div>

<br>
<br>

<div align="center">

|                                                                         **김미강**                                                                          |                                                                         **👑 양지은**                                                                          |                                                                           **이동윤**                                                                            |                                                                          **이인수**                                                                           |                                                                                **최선정**                                                                                 |                                                                          **한성민**                                                                           |
| :---------------------------------------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------------------------------: |
| [<img src="https://github.com/user-attachments/assets/aae27a59-ae1c-4239-adc1-280a8661a5bd" height=120 width=120> <br/> @mkngkm](https://github.com/mkngkm) | [<img src="https://github.com/user-attachments/assets/8ae52319-ba0a-4bbd-b9e1-7fca73c88dd7" height=120 width=120> <br/> @yje9802](https://github.com/yje9802) | [<img src="https://github.com/user-attachments/assets/6d5a2507-eec2-438d-a4e6-9d54879cf341" height=120 width=120> <br/> @leedy903](https://github.com/leedy903) | [<img src="https://github.com/user-attachments/assets/b2d4c856-8ade-4a21-aab7-16126d771f7e" height=120 width=120> <br/> @insoo00](https://github.com/insoo00) | [<img src="https://github.com/user-attachments/assets/3d2796f0-de6a-4dfc-8b4d-d9b34c6801e4" height=120 width=120> <br/> @Choeseonjeong](https://github.com/Choeseonjeong) | [<img src="https://github.com/user-attachments/assets/4ca9f591-65a0-4e2c-8d3a-340a0a5beb70" height=120 width=120> <br/> @kkx7787](https://github.com/kkx7787) |
|                                                                       채팅, 상담 예약                                                                       |                                                                        유저 인증, 검색                                                                        |                                                                            대출 추천                                                                            |                                                                       매물, 클러스터링                                                                        |                                                                              채팅, 상담 예약                                                                              |                                                                       매물, 클러스터링                                                                        |

</div>

<br>



## 개발 기간

- **프론트엔드 개발** : 2024년 10월 17일 ~ 2024년 10월 30일
- **백엔드 개발** : 2024년 12월 16일 ~ 2024년 12월 27일
- **배포** : 2024년 12월 29일
- **최종 발표 및 평가** : 2024년 12월 30일

---

<br>

## 기술 스택 
기술 스택 선정에 대한 이유는 [Wiki](https://github.com/Hanaro-OMNM/Hanasset-BE/wiki/%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D-%EC%A0%95%EC%9D%98)를 참고해주세요!

| **분류**          | **스택**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| ----------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Language**      | ![Java](https://img.shields.io/badge/Java-17-007396?style=flat&logo=openjdk&logoColor=white) ![TypeScript](https://img.shields.io/badge/TypeScript-4.5-3178C6?style=flat&logo=typescript&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| **Framework**     | ![SpringBoot](https://img.shields.io/badge/SpringBoot-3.1.1-6DB33F?style=flat&logo=springboot&logoColor=white) ![React](https://img.shields.io/badge/React-18.3.1-61DAFB?style=flat&logo=react&logoColor=black)                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| **Build**         | ![Gradle](https://img.shields.io/badge/Gradle-7.0-02303A?style=flat&logo=gradle&logoColor=white) ![Vite](https://img.shields.io/badge/Vite-4.0-646CFF?style=flat&logo=vite&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **Front-end**     | ![TailwindCSS](https://img.shields.io/badge/TailwindCSS-3.2-06B6D4?style=flat&logo=tailwindcss&logoColor=white) ![Recoil](https://img.shields.io/badge/Recoil-Experimental-3578E5?style=flat&logoColor=white) ![Axios](https://img.shields.io/badge/Axios-0.21.1-5A29E4?style=flat) ![React Naver Maps](https://img.shields.io/badge/React%20Naver%20Maps-API-61DAFB?style=flat&logo=react&logoColor=black)                                                                                                                                                                                                                                                                 |
| **Back-end**      | ![Spring Security](https://img.shields.io/badge/Spring%20Security-5.6.1-6DB33F?style=flat&logo=springsecurity&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-2.5.6-6DB33F?style=flat&logo=spring&logoColor=white) ![OAuth 2.0](https://img.shields.io/badge/OAuth%202.0-Standard-3C7EBB?style=flat&logo=oauth&logoColor=white) ![WebSocket](https://img.shields.io/badge/WebSocket-API-4A90E2?style=flat) ![STOMP](https://img.shields.io/badge/STOMP-Protocol-800000?style=flat) ![Spring Mail](https://img.shields.io/badge/Spring%20Mail-3.0.0-6DB33F?style=flat)                                                                  |
| **Data**          | ![Python](https://img.shields.io/badge/Python-3.10-3776AB?style=flat&logo=python&logoColor=white) ![BeautifulSoup4](https://img.shields.io/badge/BeautifulSoup4-WebScraping-4B8BBE?style=flat)                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| **Database**      | ![MySQL](https://img.shields.io/badge/MySQL-8.3.0-4479A1?style=flat&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-7.2.4-DC382D?style=flat&logo=redis&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| **Tool**          | ![Postman](https://img.shields.io/badge/Postman-API%20Testing-FF6C37?style=flat&logo=postman&logoColor=white) ![IntelliJ](https://img.shields.io/badge/IntelliJ%20IDEA-Backend-000000?style=flat&logo=intellijidea&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-Design-FF7262?style=flat&logo=figma&logoColor=white) ![VSCode](https://img.shields.io/badge/VSCode-Frontend-007ACC?style=flat&logo=visualstudiocode&logoColor=white) ![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-85EA2D?style=flat&logo=swagger&logoColor=white) ![Github](https://img.shields.io/badge/Github-Code%20Hosting-181717?style=flat&logo=github&logoColor=white) |
| **Deploy**        | ![EC2](https://img.shields.io/badge/AWS%20EC2-Cloud-orange?style=flat&logo=amazonaws&logoColor=white) ![AWS RDS](https://img.shields.io/badge/AWS%20RDS-Database-527FFF?style=flat&logo=amazonrds&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| **Communication** | ![Notion](https://img.shields.io/badge/Notion-Wiki-000000?style=flat&logo=notion&logoColor=white) ![Slack](https://img.shields.io/badge/Slack-Chat-4A154B?style=flat&logo=slack&logoColor=white) ![Jira](https://img.shields.io/badge/Jira-Project%20Management-0052CC?style=flat&logo=jira&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                |



<br>


## ERD


![omnm_erd](https://github.com/user-attachments/assets/3f086e05-6910-4759-a797-5fe170fa113a)

<br>

## API 명세
swagger-ui를 활용해 자동화된 문서로 관리했습니다. 자세한 API 명세는 [Wiki](https://github.com/Hanaro-OMNM/Hanasset-BE/wiki/API-%EB%AA%85%EC%84%B8%EC%84%9C)를 참고해주세요.

![omnm_api](https://github.com/user-attachments/assets/55b70a4e-e10f-4989-8712-2fec3681c0e3)

<br>

## 시스템 아키텍처

### 인프라 아키텍처

![omnm_infra_architecture](https://github.com/user-attachments/assets/1be8eb46-382d-485d-a15a-ab95eb15b450)

### FE-BE 아키텍처

![omnm_system_architecture](https://github.com/user-attachments/assets/4c47810e-610b-4c76-b811-fa1a2969d541)


<br>

## 핵심 기능 소개 

### 🏠 전월세 매물 조회
- **간편하게 부동산 매물 정보 확인**  
  - 서울시의 모든 최신 전월세 매물 정보 제공  
  - 원하는 지역의 모든 매물 위치를 한 눈에 파악 
  - 관심 가는 매물에 대한 상세한 정보 확인
    

  <img src="https://github.com/user-attachments/assets/c1a942a7-ef62-476e-8300-47a0dc19bfba" alt="Image 1" width="600"/>
  <br>
  <img src="https://github.com/user-attachments/assets/ddc13082-62e1-41ef-bf29-36ca3e4d8980" alt="Image 2" width="600"/>
    


<br>



### 💳 맞춤형 대출 상품 추천
- **자산 현황에 맞는 대출 상품 리스트 확인**  
  - 연소득, 대출 현황 등의 자산 정보를 분석하여 최적의 상품 제안
  - 대출 조건 비교 기능
  
 <img src="https://github.com/user-attachments/assets/dd0314a2-652c-4b6f-94c2-df233946ea9b" alt="Image 2-1" width="600"/>
  <br>
<img src="https://github.com/user-attachments/assets/174a81f7-4c5d-4e44-9049-7791d99ecef9" alt="Image 2-2" width="600"/>

<br>


### 💬 실시간 상담 서비스
- **대출 상담 및 계획 수립 지원**  
  - 하나은행 전문 상담사와 채팅을 통한 상세한 대출 상담 진행 
  - 대출 계획 수립 및 맞춤형 컨설팅 제공 
  - 상담 내역 저장 및 다시보기 기능
  - 예약을 통해 원하는 시간에 상담 가능 
  
<img src="https://github.com/user-attachments/assets/332b94cb-544e-4afa-a54a-68eae1200a01" alt="Image 3" width="600"/>
  
