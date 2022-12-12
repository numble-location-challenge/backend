# backend
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=SpringSecurity&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL 8-4479A1?style=flat&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Jenkins-D24939?style=flat&logo=Jenkins&logoColor=white"/> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=Swagger&logoColor=white"/>

**Numble [나만의 지역 커뮤니티 만들기] 챌린지**

**2022-11-29 ~ 2022-12-01 진행**

지역주민과 취미생활을 함께하고 소통할 수 있도록 도와주는 지역 기반 모임 서비스입니다. 6km 거리 안의 동네친구들만 참여할 수 있는 모임을 만들거나 참여할 수 있어요. 피드를 통해 친구들에게 일상을 공유해보세요.

## 사용 기술

- Spring Boot 2.7.5
- Spring Data JPA
- Spring security & JWT
- Java 11
- Build Tool: gradle
- DB: MySQL8
- CI/CD:  Jenkins 2.361.3
- Infra(PaaS): Naver Cloud Server
- Log: Slf4j
- API Docs:  Swagger3
  - springdoc-openapi v1.6.12

## 기능 및 역할분담

- **송유진**
  - 자체 회원가입 & 로그인 & 로그아웃 + 카카오 소셜 로그인
  - 회원 RUD
  - 모임 신청, 신청 취소, 강퇴 (회원—모임 연결 기능)
  - 모임 CU
  - Swagger API 설정 및 적용
- **신윤상**
  - 피드 CRUD
   - 조건 필터링(최신순, 좋아요 누른 글)
  - 댓글 & 대댓글 CRUD 및 필터링(내 댓글)
  - Public Cloud 관리 및 Jenkins를 사용한 CI/CD
- **홍수희**
  - 모임 RD
   - 조건 필터링(최신순, 인기순, 마감임박순, 내가 쓴 글, 좋아요 누른 글)
  - 좋아요 CD


## 산출 문서

- [Numble 측에서 제공한 기본 기획서](https://www.notion.so/51fb74d918234fb8b6692e473c842b65)

- [요구사항 정리](https://github.com/numble-location-challenge/backend/wiki/%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EB%A6%AC)

- [DB 및 ERD 설계](https://www.notion.so/DB-d0656ed726384bd0832a0304510a911f)

- [API 초기설계](https://www.notion.so/9794dcbdba3241c0bc8db52c0c33a172)

- [Swagger-UI API 문서](http://101.101.211.186:3100/swagger-ui/index.html)

- Demo 영상

![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/43891587/206953622-ccf2a1e7-60d3-4b1c-b3f5-442cf3a47a6c.gif)


## 패키지 구조
```
└─src
    ├─main
    │  ├─generated
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─backend
    │  │              ├─controller
    │  │              │  └─testCode
    │  │              ├─domain
    │  │              │  ├─enumType
    │  │              │  ├─post
    │  │              │  └─tag
    │  │              ├─dto
    │  │              │  ├─comment
    │  │              │  ├─feed
    │  │              │  ├─login
    │  │              │  ├─response
    │  │              │  ├─social
    │  │              │  └─user
    │  │              ├─global
    │  │              │  ├─config
    │  │              │  ├─exception
    │  │              │  │  ├─base
    │  │              │  │  ├─comment
    │  │              │  │  ├─feed
    │  │              │  │  ├─social
    │  │              │  │  └─user
    │  │              │  ├─security
    │  │              │  │  └─jwt
    │  │              │  │      └─authToken
    │  │              │  └─utils
    │  │              ├─repository
    │  │              └─service
    │  │                  ├─comment
    │  │                  ├─feed
    │  │                  ├─login
    │  │                  ├─social
    │  │                  └─user
    │  │                      └─userInfo
    │  └─resources
    └─test
```

## 서비스 컨셉 디자인

![image](https://user-images.githubusercontent.com/51091854/206086860-d1154cb7-537c-457c-831e-a6fa5b62275b.png)
![image](https://user-images.githubusercontent.com/51091854/206086916-e30d05fa-8443-4f7d-a74d-aa5eb1c7c7b3.png)
![image](https://user-images.githubusercontent.com/51091854/206086959-80240c72-577f-46d1-92f0-a3bf9074d420.png)
![image](https://user-images.githubusercontent.com/51091854/206086982-2cd2923d-ff06-4176-be12-80d7045ee5e4.png)
![image](https://user-images.githubusercontent.com/51091854/206087177-46aaeb32-cfd2-4ae4-9ff6-a8bbff3b944e.png)
![image](https://user-images.githubusercontent.com/51091854/206087206-8ff549bb-e2cc-4ae8-b923-e382233d0234.png)
![image](https://user-images.githubusercontent.com/51091854/206087274-692edc48-196d-4563-85cd-f31bc42a3c7c.png)
![image](https://user-images.githubusercontent.com/51091854/206087319-ddffe2ae-7c1e-4143-b5bd-9db1baca2baf.png)
![image](https://user-images.githubusercontent.com/51091854/206087348-d6e563a0-b03b-4d7a-ac0d-4833a48e0d91.png)
![image](https://user-images.githubusercontent.com/51091854/206087394-418c1f08-4fc9-4d08-9ef4-c304227376ae.png)
![image](https://user-images.githubusercontent.com/51091854/206087427-a7cd0d02-89c4-47b9-a65f-c6ddf6e6ebcb.png)
![image](https://user-images.githubusercontent.com/51091854/206087464-fc05047c-4372-48af-a4e0-2450f32c3b4e.png)
![image](https://user-images.githubusercontent.com/51091854/206087488-3b506e33-77f4-4078-8e7b-58187090a8ba.png)
![image](https://user-images.githubusercontent.com/51091854/206087524-012a36a4-1d0f-4b90-971c-c4344159fa04.png)
![image](https://user-images.githubusercontent.com/51091854/206087555-657b30b2-45d8-4fac-9199-370d0fbfbf4b.png)
