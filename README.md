## 👊 최강 11팀
|분류|내용|
|----|----|
|주제| 물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼|
|팀원 구성| 👑 [김준현](https://github.com/whitejh) [박지민](https://github.com/MeGuuuun) [백지연](https://github.com/rkoji)|
|개발 기간| 2024.09.05 ~ 2024.09.19|
|API 명세서| [🔗 API 명세서](https://teamsparta.notion.site/dcbbcc89e1aa45609386609486f46ef1?v=84dc165bcdd94d5e8c300ec8824ba4e3)|
|테이블 명세서|[🔗 테이블 명세서](https://teamsparta.notion.site/11-4da9c601d2cf461ca8f811e523194391)|

## 🔎 프로젝트 기능
### 🔓 인증/인가 시스템 
  - MSA 구조를 기반으로 Spring Cloud Gateway를 사용하여 JWT 토큰 기반의 인증/인가를 구현했습니다.
  - 모든 요청은 게이트웨이를 통해 각 서비스로 전달되며, 인증과 인가는 다음과 같은 절차로 처리됩니다.
  1.  인증 (Authentication)
  - 사용자는 로그인 요청을 보내면, 인증 서버가 자격 증명을 한 후 JWT Token을 발급합니다.
  2. 인가 (Authorization)
  - 모든 서비스 요청은 게이트웨이에서 JWT 토큰 검증을 거친 후 해당 서비스로 전달됩니다.
 - 서비스에서는 추가 검증 없이, @PreAuthorize로 권한을 체크하여 요청을 처리합니다.

### 🚚 배송/허브 기능
1. 주문 생성 :
- 고객의 주문이 들어오면, 주문에 포함된 상품 아이디와 수량을 사용하여 관련 업체에 Feign 클라이언트를 통해 요청합니다.
- 해당 상품의 존재 여부와 재고 수량을 확인한 후, 판매 가능 여부를 정합니다.
- 판매 가능할 경우, 재고 수량을 업데이트 합니다.
2. 배송 생성:
- 주문 생성과 동시에 배송이 생성됩니다. 이 과정은 Feign 클라이언트를 통해 이루어집니다.
- 배송 생성 시, 배송 기록도 생성됩니다. 배송 기록 생성을 위해 Feign 클라이언트를 사용합니다.
- 배송 생성 시, 허브 간 이동정보 서비스에 Feign 클라이언트를 통해 요청하여 출발 허브와 도착 허브 사이의 경로를 받아옵니다.
- 경로 정보를 배송 기록에 저장합니다.
3. 재고 롤백
- 주문 취소 시, 관련된 재고를 롤백하여 재고 수량을 원상태로 복구합니다.
- 경로 정보를 배송 기록에 저장합니다.
4. 기타 기능:
- 업체나 상품 등록 시, 허브의 존재 여부를 확인합니다.


## 💻 적용 기술
##### □ Swagger
> MSA 구조에서 여러 서비스의 API를 자동으로 문서화 하고, 실시간 테스트 및 유지보수를 쉽게 하기 위해 도입
##### □  Redis
> 데이터 캐싱을 통해 서비스 기능을 개선하고, 세션 관리나 토큰 저장과 같은 빠른 데이터 엑세스가 필요한 작업에서 처리 속도를 향상시키기 위해 도입
##### □  Zipkin
> MSA 환경에서 각 서비스 간의 호출을 추적하고, 분산 트랜잭션의 성능 문제나 장애 지점을 시각화하여 빠르게파악할 수 있도록 하기 위해 도입
##### □  FeignClient
> MSA 환경에서 서비스 간의 통신을 간소화하고, 선언적인 방식으로 API 호출을 관리하여 코드의 가독성과 유지보수성을 높이기 위해 도입

## 📐 Architecture
[Image] : Architecture 이미지 추가하기

## 📝 ERD Diagram
[Image] : erd 이미지 추가하기

## 🛠️ 개발 환경
- **Backend**
  ![Java](https://img.shields.io/badge/java17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) 
  ![Spring Boot](https://img.shields.io/badge/spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white) 
  ![Spring Security](https://img.shields.io/badge/spring%20Security-%236DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white) 
  ![Spring JPA](https://img.shields.io/badge/spring%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) 
  ![JWT](https://img.shields.io/badge/JWT-000000.svg?style=for-the-badge&logo=jsonwebtokens&logoColor=white) 
  ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=gradle&logoColor=white)

- **Architecture**
  ![Microservices Architecture](https://img.shields.io/badge/MSA-000000.svg?style=for-the-badge&logo=architecture&logoColor=white) 
  ![API Gateway](https://img.shields.io/badge/API%20Gateway-000000.svg?style=for-the-badge&logo=api-gateway&logoColor=white) 
  ![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white)

- **Tools**
  ![Github](https://img.shields.io/badge/Github-181717.svg?style=for-the-badge&logo=github&logoColor=white)
![intellij IDE](https://img.shields.io/badge/intellij%20IDE-181717.svg?style=for-the-badge&logo=intellijidea&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37.svg?style=for-the-badge&logo=postman&logoColor=white)
![Notion](https://img.shields.io/badge/notion-000000.svg?style=for-the-badge&logo=notion&logoColor=white)


