## 프로젝트 소개

---
<img width="1338" height="583" alt="image" src="https://github.com/user-attachments/assets/3d82c24a-eac4-495f-9167-8b4e43b6373d" />


FITinside는 LF몰, 무신사와 같은 온라인 쇼핑몰을 모티브로 한 웹사이트입니다.

사용자는 다양한 상품을 탐색하고, 원하는 상품을 장바구니에 담아 관리할 수 있습니다.

회원가입 및 로그인(구글 로그인 포함)을 통해 주문도 완료할 수 있으며 카테고리별 상품 분류 및 쿠폰 적용 기능 등을 추가해 사용자 경험을 향상시켰습니다.

## 팀원 구성

---

간단한 이미지랑 태그걸기

## 개발 기간

---

2024.09.30 ~ 2024.10.25 (1개월)

## 기술 스택 (버전 기입)

---

꼬리질문 3개 이상 (3줄 이상씩은 쓰기, ex. 뭐가 효율적인지?)

다른 기술스택이랑 비교해서 이 기술을 선택한 이유

Back-end
<img width="275" height="183" alt="image" src="https://github.com/user-attachments/assets/145669aa-d833-4058-8bf6-c50caa8ecad5" /> <img width="310" height="163" alt="image" src="https://github.com/user-attachments/assets/10bf3b99-5255-42cb-8ab8-4ed8dab1a1fa" /> <img width="341" height="148" alt="image" src="https://github.com/user-attachments/assets/011c6903-ba92-4971-9a6e-6b397e18f4a2" /> <img width="250" height="252" alt="image" src="https://github.com/user-attachments/assets/4070ddd4-0f14-417c-bb56-54b19dca3888" />
<img width="300" height="168" alt="image" src="https://github.com/user-attachments/assets/8daa1baf-bd2f-4ccf-950c-0379df733062" /> <img width="284" height="177" alt="image" src="https://github.com/user-attachments/assets/d93069f6-5f6c-46b2-bddb-652d9cde5dfc" /> <img width="225" height="225" alt="image" src="https://github.com/user-attachments/assets/73afaaf3-05e3-4cfd-aa03-905a970c928f" /> <img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/a14e156a-5ede-4d7d-bd03-0cc2a0f841cc" />

- Java 17 : 최신 LTS(Long-Term Support) 버전으로 안정성과 성능 제공
- Spring Boot 3.3.4 : 경량화된 자바 프레임워크, RESTful API 구축 및 서버 사이드 로직 처리하는 역할을 수행하고 이번 프로젝트 표준 기술로 채택
- Spring Security 3.3.4 : Spring Boot를 사용하여 개발하는 과정에 높은 연동성을 제공하고 인증, 권한 부여와 엑세스 제어등의 여러 기능을 편리하게 이용하여 사용자 데이터를 안전하게 보호
- JWT 0.12.1: 무상태성(State-Less)를 지향하는 HTTP의 ~~단점을 보완~~ 특성에 적합하고 로그인 정보가 필요한 부분의 확장을 용이하게하는 장점을 이용하고자 선택
- JPA 3.3.4: 개발 과정에서 여러 데이터베이스 사용하고 연동하기 위한 ORM 기술을 사용해 객체지향 언어를 사용한 Spring Boot 프로젝트에 적합  ~~효율적인 데이터베이스 처리 구현~~하고 MyBatis와 같은 SQL Mapping 기술보다 단순한 DB조작을 많이 하는 서비스를 구현하는 이번 프로젝트에 적합
    - ⇒ 내부적으로 최적화하는 부분 많음 / 면접을 위해서 공부하
- MySQL(AWS RDS): 관계형 데이터베이스, 쇼핑몰의 데이터를 저장 및 관리
- Mockito 5.11.0: 단위 테스트를 위한 Mock 라이브러리 ⇒ JUnit
- Jakarta Mail 2.0.3: 이메일 발송을 위한 라이브러리로, Spring Boot와 통합하여 비동기적으로 메일을 전송
- AWS S3 2.2.6:
    - 이미지 파일 관리를 위한 S3 설명 추가

### Front-end

- HTML, CSS : 웹 표준을 준수한 마크업 및 스타일링
- React : 컴포넌트 기반의 UI 라이브러리를 활용하여 효율적인 상태 관리 및 인터랙티브한 사용자 인터페이스 구현
- Axios: 백엔드와의 HTTP 통신을 위한 비동기 요청 처리 라이브러리

서비스 배포 환경
<img width="225" height="225" alt="image" src="https://github.com/user-attachments/assets/5d1a2e51-fcbb-481a-94e8-ebeebd58f877" /> <img width="206" height="245" alt="image" src="https://github.com/user-attachments/assets/a2b94398-6223-4aa8-98db-dc04f3672a06" /> <img width="259" height="194" alt="image" src="https://github.com/user-attachments/assets/5f4caabf-338a-4f0c-acbe-daf1bf486462" />

- 프론트엔드 배포
    - Netlify
- 백엔드 배포
    - 엘리스 클라우드 VM
        - GNU/Linux 5.15.0-91-generic
        - Ubuntu 22.04.5 LTS

### 버전 및 이슈관리

GitLab Project, GitLab Issues,  GitHub(Netlify 배포용)

### 협업 툴

Discord, Notion

### 기타

- Mapstruct 1.5.3
- Lombok 1.18.34
- Swagger 2.0.4

## 브랜치 전략

---

- Git-flow 전략을 기반으로 master, develop 브랜치와 featue 등의 기능 브랜치를 활용했습니다.
    - master : 배포 단계에서만 사용하는 브랜치입니다.
    - develop : 개발 단계에서의 master 역할을 하는 브랜치입니다.
    - review-develop : 기능 브랜치를 develop에 merge하기 전 코드 리뷰를 통해 정상적으로 동작을 하는지 확인하는 역할을 하는 브랜치입니다.
    - publish-develop : 배포 환경(배포된 서버 URL 반영, RDS 적용 등)을 적용해 놓은 브랜치입니다.
    - feature: 기능 단위로 독립적인 개발 환경을 위해 사용하고 merge 후 브랜치를 삭제해주었습니다.
    - refactor: 기존 코드를 개선하고 구조를 변경하는 브랜치입니다. review-develop으로 merge 후 브랜치를 삭제해 주었습니다.
    - test: 테스트 코드를 작성하거나 기존 테스트 코드를 리팩토링 하는 브랜치입니다. review-develop으로 merge 후 브랜치를 삭제해 주었습니다.

⇒ hotfix , release 브랜치를 안썼으니까 대응이 필요함

## 프로젝트 구조

---

- 도메인별 사용안하는 exception 패키지 삭제하기
    
    ```bash
    
    # address : 배송지
    # banner : 광고
    # cart : 장바구니
    # category : 상품 카테고리
    # coupon : 상품 쿠폰
    # global : 전역 예외
    # member : 회원
    # oath : 인증
    # order : 주문
    # produt : 상품
    
    src
    ├── main
    │   └── java
    │       └── com
    │           └── team2
    │               └── fitinside
    │                   ├── address
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── mapper
    │                   │   ├── repository
    │                   │   └── service
    │                   ├── banner
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── mapper
    │                   │   ├── repository
    │                   │   └── service
    │                   ├── cart
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── mapper
    │                   │   ├── repository
    │                   │   └── service
    │                   ├── category
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── mapper
    │                   │   ├── repository
    │                   │   └── service
    │                   ├── config
    │                   ├── coupon
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── mapper
    │                   │   ├── repository
    │                   │   └── service
    │                   ├── global
    │                   │   └── exception
    │                   ├── jwt
    │                   ├── member
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── mapper
    │                   │   ├── repository
    │                   │   └── service
    │                   ├── oath
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── repository
    │                   │   ├── service
    │                   │   └── util
    │                   ├── order
    │                   │   ├── common
    │                   │   ├── controller
    │                   │   ├── dto
    │                   │   ├── entity
    │                   │   ├── mapper
    │                   │   ├── repository
    │                   │   └── service
    │                   └── product
    │                       ├── controller
    │                       ├── dto
    │                       ├── entity
    │                       ├── image
    │                       ├── mapper
    │                       ├── repository
    │                       └── service
    └── test
        └── java
            └── com
                └── team2
                    └── fitinside
                        ├── auth
                        ├── cart
                        │   ├── controller
                        │   └── service
                        ├── coupon
                        │   ├── controller
                        │   └── service
                        └── member
                            └── service
    
                     
    
    ```
    

## 역할 분담

---

- 회원가입/로그인 → 박진영
- 카테고리/배너 → 유연주
- 상품관리 → 이하현
- 장바구니/쿠폰 → 안창민
- 주문/배송지 → 허수빈

organization 한개 만들어서 모든 팀원이 들어올것 → 각 팀원들의 깃헙 아이디를 태그 (링크 걸어놓음)

## 주요 기능

검색, 정렬, 페이지네이션 기능 추가

---






