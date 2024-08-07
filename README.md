# SNS-APP
#### 다양한 사람들이 모여 일상을 공유하는 게시판
- 배포 url : http://3.35.207.41 (AWS 퍼블릭주소)
  
### 개인프로젝트
- 프론트엔드
    - 개발 언어 : `Javascript` , `TypeScript`
    - 개발 환경 : `React.js 17.0`
        - 라이브러리 / 의존성 : `Redux-Toolkit` , `Axios`, `Styled-Components` , `Bootstrap`
    - Github: https://github.com/Jundev21/SNS-Front.git
- 백엔드
    - 개발 언어 : `Java 17`
    - 개발 환경 : `Spring Boot 3.2.1`  , `Gradle 8.5`
        - 라이브러리 / 의존성 : `Spring Web` , `Spring Security` `JWT`, `JPA`, `Lombok`, `MySql` , `Google-Api`,`Redis`
- 배포
    - Deploy : AWS EC2, Nginx, Google Drive

### 기능
- 로그인 
  - Spring Security JWT 를 활용한 로그인, 회원가입, 탈퇴
- 게시판 
  - 게시판 수정,삭제
- 나의 게시판 
  - 내가 작성한 게시판 목록
- 댓글 
  - 게시판 댓글 작성
- 마이페이지 
  - 사용자 정보 ,이미지 프로필 , 이메일, 비밀번호 -  수정
- 검색 
  - 제목과 내용에 포함된 모든 작성내용 검색
- 게시판 필터링
  - 날짜 최신순, 좋아요 수 , 댓글 수 기준으로 게시판 필더링 
- 알림 
  - 사용자가 작성한 게시글에 댓글 , 좋아요를 추가할 경우 실시간 알림 / 알림 목록란에서 알림 클릭시 해당 게시물로 이동

### 아키텍쳐 

![Screenshot 2024-07-05 at 5 20 50 PM](https://github.com/Jundev21/SNS-Back/assets/55421772/5d679b5a-d00f-4f04-aee0-a8f4b172e535)

### Swagger API

url : http://3.35.207.41/swagger-ui/index.html

### ERD 다이어그램
<img width="851" alt="Screenshot 2024-07-05 at 2 38 42 PM" src="https://github.com/Jundev21/SNS-APP/assets/55421772/69d6921a-925e-45b4-88a2-1b9bcf46804f">

