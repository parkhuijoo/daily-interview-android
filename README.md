##### Mobile Programming Project
<img width="100" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/logo.png">
# Daily Interview
소프트웨어학부 20181299 박희주
https://github.com/parkhuijoo/daily-interview-android 에서 확인하실 수 있습니다.

---

### 개발 환경 및 라이브러리
- Android Studio
- MacOS 11.0.1 Big Sur
- Firebase Realtime Database
- Java
- Kotlin
- LastAdapter
- DataBinding
- Glide
- CircleImageView
- Target SDK 30, min SDK 23

* Firebase 연동이 되어있어 소스코드를 직접 빌드하면 key 누락으로 정상 실행되지 않습니다.(SHA-1 키 등록 필요) apk폴더에 테스트를 위한 릴리즈 apk를 업로드했으니, 채점에 사용하시면 됩니다!

---

### 프로젝트 소개

##### 1. 로그인 및 회원가입

<img width="250" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/1.png">

- 앱에 접속하면 가장 먼저 보이는 화면, 이름과 생년월일을 입력해 회원가입 및 로그인할 수 있음
- 필드를 하나라도 공란으로 두고 로그인을 시도할 경우 토스트 노출
- 기존에 존재하는 이름인 경우 로그인을 시도하고, 존재하지 않는 이름인 경우 자동으로 회원가입
- 존재하는 이름이지만, 생일 정보가 틀린 경우 오류메시지를 띄워줌
- 한번 로그인/회원가입할 경우 이후에는 자동로그인으로 넘어감

##### 2. 메인 화면 / 이달의 답변과 오늘의 질문

<div>
<img width="250" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/2-1.png">
<img width="250" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/2-2.png">
</div>

- 이달의 우수 답변을 선정해 메인 화면에 노출시켜줌
- 우수 답변은 운영자가 데이터베이스에서 직접 관리(실시간 반영)
- 우수 답변 카드를 클릭하여 답변을 Dialog에서 확인할 수 있음
- 하단 카드뷰를 통해 오늘의 질문을 확인하고 바로 답변할 수 있도록 구성(질문 목록 중 가장 최신 질문을 노출)
  
##### 3. 질문 목록 화면

<img width="250" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/3.png">

- 질문 목록들이 날짜, 질문으로 구성된 리사이클러뷰로 노출됨
- 질문 카드를 눌러 해당 질문의 답변 화면으로 넘어갈 수 있음
- 해당 질문과 어울리는 사진URL을 서버에서 받아와 같이 보여줌

##### 4. 프로필 화면 / 정보 확인 및 편집

<img width="250" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/4.png">

- 회원가입시 입력한 이름과 생년월일을 확인할 수 있음
- 희망 회사와 직렬을 입력해 목표를 설정할 수 있음
- 설정한 목표는 파이어베이스 DB 유저데이터에 저장됨

##### 5. 질문 상세 화면 / 답변 작성 및 다른 사람의 답변 확인

<div>
<img width="250" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/5-1.png">
<img width="250" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/5-2.png">
</div>

- 공통적으로 질문이 보여지며, 내가 작성한 답변이 없을 경우 다른 사람들의 답변을 확인할 수 없으며, 답변 작성 화면이 나타남
- 답변은 3분 내에 저장해야하며, 3분을 초과하여 저장 버튼을 클릭하면 답변 시간을 초과했다는 문구로 답변이 등록됨
- 답변란을 공란으로 저장하려고 할 경우 답변을 입력하라는 토스트 노출
- 입력 중간에 뒤로가기를 통해 액티비티를 종료할 경우 작성을 포기했다는 문구로 답변이 등록됨
- 한번 작성된 답변은 수정이나 삭제할 수 없으며, 이는 한번 뱉은 말은 주워 담을 수 없는 면접의 특성을 앱에서 구현하기 위함임
- 내 답변을 작성하면 다른 사람들의 답변이 보여지고, 내 답변과 비교하며 공부가 가능

---

### 특이사항

> Firebase Realtime Database를 이용하여 실시간으로 추가/삭제/변경, 앱에 반영할 수 있음
> Database의 구조는 다음과 같음
<img width="500" src="https://github.com/parkhuijoo/daily-interview-android/blob/main/imgs/db.png">

> 외부 오픈소스 라이브러리를 적극적으로 활용했는데, 이 과정에서 일부 라이브러리가 코틀린으로 작성되었고, 코틀린만 제대로 지원하여 ListFragment는 코틀린으로 작성하였음
> 코틀린과 자바를 혼용하기 위해 프로젝트 설정을 변경하였음

> 리사이클러뷰를 구현하는 과정에서 LastAdapter, DataBinding을 함께 사용하여 xml에서 객체의 값을 바로 주입하고, 리사이클러뷰에 반드시 필요한 어댑터를 LastAdapter 코드 한 줄로 대체하여 코드의 직관성이 대폭 높아졌음

