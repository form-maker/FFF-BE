## ☁️ [폼폼폼] 사용자 친화적인 설문조사 폼 <br/>
### 아기자기 몽글몽글 설문조사 
![메인](https://user-images.githubusercontent.com/101397314/216868532-a5e4005b-d14f-4ea6-85e4-6a6ec9a318a3.png)

### [☁️ 폼폼폼 둘러보기](www.foamfoamform.com)

---
### 💭 기획
***“딱딱한 설문은 싫어! 아기자기한 설문지 어디없니?”***<br/>
<br/>
불편한 터치, 답답한 글씨 사이즈, 떨어지는 가독성<br/>
기존 설문지에서 나타나는 문제를 폼폼폼은 직관적이고 사용하기 편안하게 풀었습니다.

- ***기획 기간*** : 2022.12.30 ~ 2023.01.05
- ***개발 기간*** : 2023.01.06 ~ 2023.02.09

### 👥 팀원소개

|역할|이름|분담|
|-----------|------------|-------------------------------|
|BE🔰|김범준|설문 생성, 다운로드|
|BE|김형준|인프라 구축, 기타 서비스 로직(이메일 인증, 설문 통계)|
|BE|황보석|설문 삭제 , 회원가입 로그인 , 구글로그인 , JWT , Security , RefreshToken , 설문 통계|

---
### 🔎 주요 기능 

***1) 설문을 제작할 땐 미리보기로 모바일 유저들의 사용성 체크*** <br/>
> 모바일 유저들을 타겟으로 사용성을 체크하며, 깔끔하게 제작할 수 있습니다.<br/>
설문의 특성에 따라 제작할 수 있도록, 총 9가지의 폼을 구성하였습니다.

![Group 213](https://user-images.githubusercontent.com/101397314/216897782-ea66002f-eb95-4abb-a3d2-9d4e6e32805a.png)

***2) 좀 더 많은 참여자를 원할 땐 선물하기 기능으로 관심 UP!*** <br/>
> 선물하기 기능, 참여 중인 다른 사람 수를 통해 그저 재미없는 설문을 즐겁게 참여할 수 있도록 하였습니다.

![Group 212](https://user-images.githubusercontent.com/101397314/216897768-9bbc5ff1-6a86-4cfc-adb4-f4f174cd91c1.png)

***3)설문 타입 맞춤 한눈에 볼 수 있는 통계 자료*** <br/>
> 설문 제작자에게 각 폼에 따라 다양한 통계 타입을 제공합니다.<br/>
선택 기간에 응답된 설문들의 통계 조회가 가능하며, 엑셀로 데이터를 받아 볼 수 있습니다.

![Group 214](https://user-images.githubusercontent.com/101397314/216897793-40d2ce1f-62d6-4e40-80b1-678034852992.png)

---
### ⚙ 아키텍처
![image](https://user-images.githubusercontent.com/101397314/216868691-59fe224c-d9c6-4dc2-b92a-db54cef74592.png)

### 🛠️ 기술 스택
**✔︎ Tech**<br/>
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">
<img src="https://img.shields.io/badge/redux-764ABC?style=for-the-badge&logo=redux&logoColor=white">
<img src="https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white"><br/>
<img src="https://img.shields.io/badge/reactRouterDom-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white"><br/>
<img src="https://img.shields.io/badge/styledcomponents-DB7093?style=for-the-badge&logo=styledcomponents&logoColor=white"> <img src="https://img.shields.io/badge/amazonS3-569A31?style=for-the-badge&logo=amazonS3&logoColor=white">

**✔︎ Tools**<br/>
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

---
### 🔧 주요 이슈
|이슈|설명|
|------|-------------------------------------|
|Insert Query|설문 생성시 생성되는 데이터마다 Insert 문이 많이 나가 SEQUENCE 테이블과 프로시저, 값을 가져오는 함수를 따로 만들어 SEQUENCE를 고려해봤지만 mySQL 에서는 IDENTITY 가 1개의 Flush를 기준으로 성능과 속도적인 측면에서 제일 우세할뿐만 아니라 통합적으로도 우세하여 IDENTITY 사용을 채택함.|
|Redis|회원 가입 시 이메일 인증 코드, RefreshToken 등의 데이터는 만료 기간이 정해진 데이터로 활용을 할 계획이었습니다.<br/>따라서 반영구적으로 저장되어야 하는 데이터와 분리해야할 데이터라고 판단했습니다. Redis 는 데이터가 유지될 기간을 정할 수 있으며, 캐시에 정보를 담기에 빠른 조회가 가능합니다.위와 같은 이유로 생명을 다한 데이터는 빠르게 제거할 수 있고, 데이터로의 접근이 빠른 Redis 를 사용하였습니다.|
|github actions|백엔드 코드의 취합과 배포의 과정을 일정부분 편리하게 간소화할 수 있기 떄문에 CI/CD 도입하기로 결정하였습니다.|
|nginx|비용과 certbot 을 통해 ssl 인증서 발급과 갱신을 편리하게 사용할 수 있어서 사용하게 되었습니다.|

### 🔥 트러블 슈팅

> **통계 로직**<br/>
❓도메인 설정하고 CloudFront를 사용하여 https를 적용한 후, 변경 내용이 적용이 되지 않았습니다.<br/>
❗️1차적으로 도메인 명과 버킷명을 동일하게 변경하였습니다. 그 후 CloudFront의 Invalidations(무효화)를 진행하여 Edge Location에 저장된 캐시를 삭제하여 변경 내용을 도메인 페이지에 적용하였습니다.
