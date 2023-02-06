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
|FE🔰|안수빈|배포, 메인 페이지, 메인페이지 무한 스크롤, 제작 페이지, 폼 형식 따른 수정 삭제 추가 제작, 반응형,<br/> 마이페이지, 마이페이지 조회, 설문 응답 페이지, 통계 페이지, 설문 형식별 통계 그래프 조회|
|FE|김동균|회원가입,이메일 인증,  로그인, 소셜 로그인(카카오, 구글)|

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

**✔︎ Design**<br/>
<img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">

**✔︎ Tools**<br/>
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

---
### 🔧 주요 이슈
|이슈|설명|
|------|-------------------------------------|
|CloudFront|사용자에게 제공되는 정적 컨텐츠의 전송 속도를 높이고 HTTPS를 적용시키기 위해 사용하였습니다.|
|redux-toolkit|context API와 리코일, 리덕스 툴킷을 비교 후, 리덕스 툴킷을 선택하였습니다. 리덕스 툴킷은 리덕스의 사용성과 보일러 플레이트를 보안하였고, 데이터가 복잡할 때 흐름을 파악하는데 용이하였습니다.|
|Apex-charts|데이터를 분석하는 입장에서도 용이하도록 제작하였습니다. APEX 라이브러리는 다양한 형식, 눈을 끌 수 있는 애니메이션, 한눈에 볼 수 있는 깔끔한 UI로 저희 프로젝트에 적합한 라이브러리였습니다.|


### 🔥 트러블 슈팅

> **배포**<br/>
❓도메인 설정하고 CloudFront를 사용하여 https를 적용한 후, 변경 내용이 적용이 되지 않았습니다.<br/>
❗️1차적으로 도메인 명과 버킷명을 동일하게 변경하였습니다. 그 후 CloudFront의 Invalidations(무효화)를 진행하여 Edge Location에 저장된 캐시를 삭제하여 변경 내용을 도메인 페이지에 적용하였습니다.
