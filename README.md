# 서점 POS 프로그램

서점의 카운터에서 결제를 쉽고 편하게 할 수 있도록 만든 프로그램입니다.  
계산, 회원 포인트 적립, 예약을 할 수 있고, 결제정보를 조회할 수 있으며 영업이 끝나면 매출을 표시해주는 POS 프로그램 입니다.  

실제 POS는 바코드로 상품정보를 읽어오지만, 이 기능은 구현할 수 없어서 이 프로그램은 직원이 직접 ISBN을 입력하는 것만 가능합니다.  
예약 기능 또한 마찬가지로 직원이 직접 회원조회를 하고 예약도서를 추가하는 방식으로만 예약이 이루어집니다.  

또한 이 프로그램은 POS의 목적인 구매, 결제에 초점이 맞춰져있습니다.  
따라서 이 프로그램에서는 도서정보, 사원정보, 회원정보를 사용하지만 그에 대한 관리는 하지 않습니다.   
해당 정보들은 미리 DB에 등록시켜 놓고, POS프로그램에서는 해당 정보들에 대한 조회만 가능합니다.  
이 프로그램에서 관리하는 정보는 구매, 결제, 예약정보입니다.  

프로젝트 진행 기간 : 2022.11 ~ 2022.12
<br>

# 사용 기술 및 환경
Language : <a href="https://www.java.com/ko/">Java</a>  
Database : <a href="https://www.mysql.com/">Mysql</a>  
SDK : <a href="https://eclipseide.org/">Eclipse</a>  
<br>

# 프로그램 구조도
![image](https://github.com/yuzy1022/Book-Store-POS/assets/112682861/4489f3cc-a51a-4f39-b25c-24230bad4d2c)
<br>

# DB 구조
![ERD](https://github.com/yuzy1022/Book-Store-POS/assets/112682861/7345e45c-1ed5-41e6-b3ae-5e5146610792)  
![릴레이션 스키마](https://github.com/yuzy1022/Book-Store-POS/assets/112682861/5a3cd7e2-79a9-419f-a486-1ce9fd7e4216)  
<br>

# 화면 구성 및 세부 기능
![Web 1920 – 1](https://github.com/yuzy1022/Book-Store-POS/assets/112682861/2ee457da-2a38-454c-a3ea-44f2a5104e0d)  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%99%94%EB%A9%B4">로그인 화면</a>  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%EC%B2%AB-%ED%99%94%EB%A9%B4">첫 화면</a>  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%EB%A9%94%EC%9D%B8-%ED%99%94%EB%A9%B4">메인 화면</a>  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%ED%9A%8C%EC%9B%90-%EC%A1%B0%ED%9A%8C">회원 조회</a>  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%EA%B2%B0%EC%A0%9C-%EC%88%98%EB%8B%A8">결제 수단</a>  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%EC%98%88%EC%95%BD-%EA%B4%80%EB%A6%AC">예약 관리</a>  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%EA%B2%B0%EC%A0%9C%EC%A0%95%EB%B3%B4">결제 정보</a>  
<a href="https://github.com/yuzy1022/Book-Store-POS/wiki/%ED%99%94%EB%A9%B4-%EB%B0%8F-%EC%84%B8%EB%B6%80-%EA%B8%B0%EB%8A%A5#%EB%A7%A4%EC%B6%9C-%EC%A0%95%EB%B3%B4">매출 정보</a>  
<br>

# 팀
<a href="https://github.com/yuzy1022">박상원</a>, <a href="https://github.com/DapsipniPotato">박재현</a>, <a href="https://github.com/SongHyeongJin">송형진</a>
