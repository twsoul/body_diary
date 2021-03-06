
# body_diary (포트폴리오 버전)

<body_diary pf_version>

첫 안드로이드 프로젝트 

<기능>
* 비밀번호 로그인/설정
* 지문로그인
* 자동 로그인
* 워너비 사진 등록
* 몸/식단 사진 저장
* 식단/운동 텍스트 메모
* 몸자랑 게시판
* 건강 정보/ 워너비 이미지 크롤링
* 영양 성분 도출 
* 피드백 보내기

<사용 기술>
* 언어 - java
* 라이브러리/api - vision, sqlite(android), google firebase, vision api, card view, picasso, gson, jsoup

 
<영상>

https://www.youtube.com/embed/dz-I5gUQm3E



<세부 설명 >
1. 비밀 번호 설정 
      - 다이어리 비밀번호를 설정할 수 있습니다. (숫자 4자리)
      - pw / pw 확인 --> 일치/불일치 확인 후  텍스트가 바뀝니다.
      - 비밀번호를 최초 1회 설정하면 현재 pw를 입력하고 비밀번호를 바꿀 수 있습니다.
      
2. 지문 로그인
    - 지문 인식 하드웨어가 없는 디바이스는 버튼이 비활성화 됩니다. / 지문 인식이 설정 되어 있지 않은 디바이스는 설정 방법을 toast로 알려줍니다.
    - 디바이스 내부에 저장되어 있는 지문데이터와 일치하면 로그인이 됩니다.
    
3. 자동 로그인
    - 체크하고 로그인을 하게 되면 다시 앱을 실행할 때 자동으로 로그인이 됩니다.
    - 로그아웃 기능

4. 워너비 사진 등록
    - 자신이 원하는 몸의 사진을 저장할 수 있습니다.

5. 몸사진 저장 갤러리
    - 하루하루 자신의 변하는 몸 사진을 저장 할 수 있습니다.
    - 슬라이드 쇼 기능
    - 검색 기능
    - 자신의 몸 사진을 공유할 수 있습니다.
    
6. 식단 기록
    - 자신이 먹은 아침/점심/저녁 식사를 텍스트 형태로 저장합니다.
    
7. 식단 사진
    - 자신이 먹은 음식 사진을 저장하고 간단한 메모를 남길 수 있습니다.

8. 운동 메모 
    - 오늘 자신이 할 운동의 루틴이나 운동 기록을 텍스트 형태로 저장할 수 있습니다.
 
9. 알림 설정
    - 운동 사진을 남길 시간,요일(반복)을 설정할 수 있습니다. 

10. 몸자랑 게시판
    - 닉네임을 설정할 수 있습니다. 
    - 자신의 몸을 사진을 공유할 수 있습니다. 

11. 건강 정보 
    - 액티비티를 실행시킬 때마다 네이버 건강 뉴스 부분을 크롤링해와서 보여줍니다.( 10개 아이템)
    - 이미지, 제목, 시간, 뉴스사를 크롭하고 보여주고 아이템 클릭시 뉴스 url로 넘어가 뉴스를 볼수 있습니다.
    
12. 워너비 사진 정보
    - 네이버 이미지 검색에서 이미지를 크롤링 해와 보여줍니다.( 50개 아이템)
    - 클릭을 하면 사용자의 갤러리로 사진을 다운 받을 수 있습니다.
    
13. 영양 성분 도출하기
    - 이미지를 올리면 이미지를 분석하여 텍스트를 검출 해냅니다.
    - 검출된 텍스트를 길게 누르면 드래그 후 복사 또는 공유가 가능합니다.
    
14. 피드백 보내기
    - 개발자에게 피드백을 보낼수 있는 기능입니다.
    
    
    
    
    
<연락처> 

이메일: 1013cm@naver.com
