# Unicorn Backend

가상 전시/박람회 공간에서 기업(Corp)이 브랜드 부스를 꾸미고, 개인 참가자(Indiv)가 둘러보며 광고·설문에 참여하고, 그 결과를 AI 리포트로 돌려받는 서비스의 백엔드입니다. Spring Boot 기반의 REST API 서버로, 메인 데이터는 MySQL에, 3D 브랜드 공간 데이터는 MongoDB에 저장합니다.

## Tech Stack

- **Language / Runtime**: Java 17, Spring Boot 3.2.8
- **Database**: MySQL (JPA/Hibernate), MongoDB (브랜드 스페이스 문서)
- **Auth**: Spring Security + JWT (Stateless)
- **Infra**: AWS S3(파일 업로드), Firebase Admin SDK, Docker / Elastic Beanstalk
- **API 문서**: springdoc-openapi (Swagger UI)
- **모니터링**: Actuator + Micrometer(Prometheus)
- **외부 연동**: OpenFeign (리포트 분석 서버 호출)

## 도메인 구성

| 패키지 | 설명 |
| --- | --- |
| `auth` | 기업/개인 회원가입·로그인, JWT 발급 |
| `ad` | 광고 등록 및 AI 생성 광고(`/ad/generated`) |
| `meeting` | 좌담회(미팅) 생성/조회, 참가자 상태 관리 |
| `survey` | 설문 문항·답변(텍스트/음성/선호도) 등록 |
| `report` | 설문 응답에 대한 AI 분석 리포트(감성/주제/임베딩 등) 조회 |
| `space` | 브랜드 공간(3D) 구성 요소(아이템, 조명, BGM, 도배 등) — MongoDB |
| `bookmark` | 개인 ↔ 기업 간 즐겨찾기(구독) |
| `point` | 활동 기반 포인트 적립/조회 |

API는 전부 `/api/v1/**` 하위에 있으며, 인증이 필요한 요청은 `Authorization: Bearer <JWT>` 헤더를 사용합니다.

## 로컬 실행

```bash
./gradlew bootRun
```

다음 환경 변수가 필요합니다 (`application.yml` 참고):

```
DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD   # MySQL
MONGODB_URI                                           # MongoDB (브랜드 스페이스)
ACCESS_KEY, SECRET_KEY                                # AWS S3
JWT_SECRET                                            # JWT 서명 키
```

로컬에서는 `src/main/resources/application.properties`에 위 값을 채워 넣으면 되며, 이 파일은 `.gitignore`에 등록되어 있어 저장소에는 올라가지 않습니다.

## 테스트

```bash
./gradlew test
```

`src/test`에 서비스 레이어 단위 테스트가 있습니다 (`PointServiceTest`, `BookmarkServiceTest` 등).

## Swagger

애플리케이션 실행 후 `/swagger-ui/index.html`에서 전체 API 스펙을 확인할 수 있습니다.
