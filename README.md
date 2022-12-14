# WEET ๐โโ๏ธ
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=SpringSecurity&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL 8-4479A1?style=flat&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Jenkins-D24939?style=flat&logo=Jenkins&logoColor=white"/> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=Swagger&logoColor=white"/>

**[Numble ๋๋ง์ ์ง์ญ ์ปค๋ฎค๋ํฐ ๋ง๋ค๊ธฐ](https://www.numble.it/e3d67139-f040-47cd-80a7-12e063ef1f36) ์ฑ๋ฆฐ์ง**

**2022-10-21 ~ 2022-12-01 ์งํ**

์ง์ญ์ฃผ๋ฏผ๊ณผ ์ทจ๋ฏธ์ํ์ ํจ๊ปํ๊ณ  ์ํตํ  ์ ์๋๋ก ๋์์ฃผ๋ ์ง์ญ ๊ธฐ๋ฐ ๋ชจ์ ์๋น์ค WEET ์๋๋ค. 6km ๊ฑฐ๋ฆฌ ์์ ๋๋ค์น๊ตฌ๋ค๋ง ์ฐธ์ฌํ  ์ ์๋ ๋ชจ์์ ๋ง๋ค๊ฑฐ๋ ์ฐธ์ฌํ  ์ ์์ด์. ํผ๋๋ฅผ ํตํด ์น๊ตฌ๋ค์๊ฒ ์ผ์์ ๊ณต์ ํด๋ณด์ธ์.

## ์ฌ์ฉ ๊ธฐ์ 

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

## ๊ธฐ๋ฅ ๋ฐ ์ญํ ๋ถ๋ด

- **์ก์ ์ง**
  - ์์ฒด ํ์๊ฐ์ & ๋ก๊ทธ์ธ & ๋ก๊ทธ์์ + ์นด์นด์ค ์์ ๋ก๊ทธ์ธ
  - ํ์ RUD
  - ๋ชจ์ ์ ์ฒญ, ์ ์ฒญ ์ทจ์, ๊ฐํด (ํ์โ๋ชจ์ ์ฐ๊ฒฐ ๊ธฐ๋ฅ)
  - ๋ชจ์ CU
  - Swagger API ์ค์  ๋ฐ ์ ์ฉ
- **์ ์ค์**
  - ํผ๋ CRUD
    - ์กฐ๊ฑด ํํฐ๋ง(์ต์ ์, ์ข์์ ๋๋ฅธ ๊ธ)
  - ๋๊ธ & ๋๋๊ธ CRUD ๋ฐ ํํฐ๋ง(๋ด ๋๊ธ)
  - Public Cloud ๊ด๋ฆฌ ๋ฐ Jenkins๋ฅผ ์ฌ์ฉํ CI/CD
- **ํ์ํฌ**
  - ๋ชจ์ RD
    - ์กฐ๊ฑด ํํฐ๋ง(์ต์ ์, ์ธ๊ธฐ์, ๋ง๊ฐ์๋ฐ์, ๋ด๊ฐ ์ด ๊ธ, ์ข์์ ๋๋ฅธ ๊ธ)
  - ์ข์์ CD


## ์ฐ์ถ ๋ฌธ์

- Numble ์ธก์์ ์ ๊ณตํ ๊ธฐ๋ณธ ๊ธฐํ์(๋ธ์ถ ๊ธ์ง)

- [์๊ตฌ์ฌํญ ์ ๋ฆฌ](https://github.com/numble-location-challenge/backend/wiki/%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EB%A6%AC)

- [DB ๋ฐ ERD ์ค๊ณ](https://www.notion.so/DB-d0656ed726384bd0832a0304510a911f)

- [API ์ด๊ธฐ์ค๊ณ](https://www.notion.so/9794dcbdba3241c0bc8db52c0c33a172)

- [Swagger-UI API ๋ฌธ์](http://101.101.211.186:3100/swagger-ui/index.html)

- Demo ์์

![ezgif com-gif-maker (2)](https://user-images.githubusercontent.com/43891587/206969296-af2f2431-9b54-488d-972a-3a2321c18b8d.gif)


## ํจํค์ง ๊ตฌ์กฐ
```
โโsrc
    โโmain
    โ  โโgenerated
    โ  โโjava
    โ  โ  โโcom
    โ  โ      โโexample
    โ  โ          โโbackend
    โ  โ              โโcontroller
    โ  โ              โ  โโtestCode
    โ  โ              โโdomain
    โ  โ              โ  โโenumType
    โ  โ              โ  โโpost
    โ  โ              โ  โโtag
    โ  โ              โโdto
    โ  โ              โ  โโcomment
    โ  โ              โ  โโfeed
    โ  โ              โ  โโlogin
    โ  โ              โ  โโresponse
    โ  โ              โ  โโsocial
    โ  โ              โ  โโuser
    โ  โ              โโglobal
    โ  โ              โ  โโconfig
    โ  โ              โ  โโexception
    โ  โ              โ  โ  โโbase
    โ  โ              โ  โ  โโcomment
    โ  โ              โ  โ  โโfeed
    โ  โ              โ  โ  โโsocial
    โ  โ              โ  โ  โโuser
    โ  โ              โ  โโsecurity
    โ  โ              โ  โ  โโjwt
    โ  โ              โ  โ      โโauthToken
    โ  โ              โ  โโutils
    โ  โ              โโrepository
    โ  โ              โโservice
    โ  โ                  โโcomment
    โ  โ                  โโfeed
    โ  โ                  โโlogin
    โ  โ                  โโsocial
    โ  โ                  โโuser
    โ  โ                      โโuserInfo
    โ  โโresources
    โโtest
```

## ์๋น์ค ์ปจ์ ๋์์ธ

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
