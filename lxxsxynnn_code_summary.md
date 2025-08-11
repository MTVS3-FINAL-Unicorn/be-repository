### lxxsxynnn 작업 기반 프로젝트 분석 리포트

- 리포지토리: `Unicorn-be`
- 기준 작성자 필터: `git log --author="lxxsxynnn"`
- 생성일: 2025-08-11 (자동 생성)

## 1) Language / Framework / Database / Tools 식별
- **백엔드 프레임워크**: Spring Boot 3.2.8 (`build.gradle`)
  - Spring Web, Spring Security, Spring Validation, AOP, Actuator
  - Spring Data JPA, Spring Data MongoDB, OpenFeign
- **클라우드/인프라**:
  - AWS S3 (`spring-cloud-starter-aws:2.2.6.RELEASE`)
  - Prometheus Micrometer (`io.micrometer:micrometer-registry-prometheus`)
  - Docker 배포 (`Dockerfile`, `Dockerrun.aws.json`) [추정: Elastic Beanstalk]
- **인증/JWT**: `jjwt 0.11.2`
- **API 문서화**: `springdoc-openapi-starter-webmvc-ui:2.2.0` (Swagger UI)
- **로깅**: Logback + `logstash-logback-encoder:7.4`, AOP 로깅 (`LoggingAspect`)
- **파일/미디어 관리**: AWS S3, Firebase Admin SDK 9.1.1
- **기타**: JSON (`org.json:20231013`)
- **Java/JDK**: JDK 17 (Gradle toolchain)
- 프론트엔드: 없음

근거 인용:
- `build.gradle` 주요 의존성과 설정
- `Dockerfile` 런타임/환경 변수

## 2) DB 분석
- **벤더 판별**:
  - 관계형 DB: MySQL (드라이버 `com.mysql.cj.jdbc.Driver`, `spring.jpa.database: mysql`, `hibernate.ddl-auto: update`)
  - 문서형 DB: MongoDB (URI 존재; 예: `mongodb://user:****@<host>:27017/brand_space_db`)
- **접속/환경설정**: `src/main/resources/application.yml`
  - RDBMS 연결 정보: `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` (환경변수)
  - MongoDB 연결 정보: 고정 URI 설정

- **테이블/도큐먼트 목록 및 주요 컬럼** [엔티티/도큐먼트 소스 기준]
  - JPA 엔티티 (테이블 수: 10)
    - `Ad(adId, corpId, fileUrl, previewUrl, description, adVideoUrl)`
    - `Corp(id, email, password, brandName, picName, binNo, contact, categoryId, authority)`
    - `Indiv(id, email, password, name, nickname, birthDate, gender, contact, categoryId[list], authority)`
    - `Bookmark(id, corpId, indivId)` (+ 상속 `BaseTimeEntity` 타임스탬프)
    - `Meeting(meetingId, meetingTitle, participantAgeStart/End, rewardType/rewardPrice, dates/times, corpId, categoryId, hasDeleted, isExpired, participantGender[list], participantStatus[list of embedded Participant])`
    - `PointTransaction(id, userId, activityType, points, location, timestamp)`
    - `Report(reportId, meetingId, questionId[nullable], analysisType[enum], analysisResult[LONGTEXT])`
    - `MeetingSummary(id, meetingId, corpId, summary[TEXT])`
    - `Question(questionId, meetingId, content, type[enum], options[list of image URLs])`
    - `Answer(answerId, meetingId, questionId, indivId, content)`
  - MongoDB 도큐먼트 (컬렉션 수: 1)
    - `BrandSpace(_id, corpId, items[itemId, transform{location(x,y,z), rotation(pitch,yaw,roll), scale(x,y,z)}], bgm, lighting{color,intensity}, qna{question,answer}[], papering{index,materialId}[])`

- **마이그레이션/매퍼 파일**: 전용 SQL 마이그레이션/매퍼 파일 미존재. 테이블 스키마는 JPA 엔티티로부터 자동 생성/갱신되는 것으로 추정.

## 3) 규모 산출
- **언어별 파일 수**: (명령: `git ls-files | awk -F. '/\./{print $NF}' | sort | uniq -c | sort -nr`)
  - Java: 88
  - yml: 2
  - xml: 2
  - gradle: 2
  - 기타(배포/설정 등): gitignore, jar, bat, md 등 소수
- **LOC 총합**: 5,701 (명령: `git ls-files -z | xargs -0 wc -l | tail -n1`)
- **DB 테이블 수**: 10 (명령: ripgrep 패턴 `@Entity`로 산정)
- **전체 커밋 수**: 357 (명령: `git rev-list --count HEAD`)
- **작성자 커밋 수/비율 (lxxsxynnn)**: 357 / 100.0% (명령: `git log --author="lxxsxynnn" | wc -l`)

주의: 확장자/LOC 집계는 저장소 내 모든 트래킹 파일 기준. 일부 비소스/생성물 포함 가능.

## 4) 개발환경
- **JDK/런타임**: Java 17 (Temurin 기반 도커 이미지 `eclipse-temurin:17`)
- **빌드**:
  - Gradle Wrapper: `./gradlew build`
- **실행**:
  - 로컬: `./gradlew bootRun`
  - JAR: `java -jar build/libs/*.jar`
  - Docker:
    - Build: `docker build -t unicorn-be --build-arg DATABASE_URL=... --build-arg DATABASE_USERNAME=... --build-arg DATABASE_PASSWORD=... --build-arg ACCESS_KEY=... --build-arg SECRET_KEY=... --build-arg JWT_SECRET=... --build-arg AWS_REGION=ap-northeast-2 .`
    - Run: `docker run -p 319:319 --env DATABASE_URL=... --env DATABASE_USERNAME=... --env DATABASE_PASSWORD=... --env ACCESS_KEY=... --env SECRET_KEY=... --env JWT_SECRET=... --env AWS_REGION=ap-northeast-2 unicorn-be`
- **테스트**: `./gradlew test`
- **필수 환경변수** (`application.yml`, `Dockerfile` 근거)
  - `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
  - `ACCESS_KEY`, `SECRET_KEY` (AWS S3)
  - `JWT_SECRET`
  - `AWS_REGION` (예: `ap-northeast-2`)
  - 서버 포트: 319 고정 (yml)

## 5) lxxsxynnn 담당 모듈
- **수정 파일 범위**: 고유 수정 파일 수 247 (명령: `git log --author ... --name-only | sort -u | wc -l`)
- **주요 패키지/레이어** (작성자 변경 이력 기준 그룹핑)
  - Controller: `ad`, `auth`, `bookmark`, `meeting`, `point`, `report`, `survey`, `space`
  - Service: `ad`, `auth`, `bookmark`, `meeting`, `point`, `report`, `survey`, `space`
  - Repository: `ad`, `auth`, `bookmark`, `meeting`, `point`, `report`, `survey`, `space` (+ `MongoRepository` for `BrandSpace`)
  - Entity/DTO: 위 도메인 전반의 `Entity`/`DTO` 생성/수정
  - Config: `SecurityConfig`, `SwaggerConfig`, `WebConfig`, `FeignClientConfig`, `MongoConfig`, `S3Config`, `FirebaseConfig`
  - Infra/Deploy: `Dockerfile`, `Dockerrun.aws.json`, GitHub Actions `docker-image.yml`
  - Logging/Monitoring: `LoggingAspect`, Logback/Logstash 설정, Actuator/Prometheus

- **대표 커밋 Top N (요지)**
  - 13c94b7 (2024-11-11): AI 통신 위한 좌담회 제출 로직 수정 + Feign 구조 정리 (19 files, +256/-178)
  - f1222c7 (2024-11-09): 보고서 생성 요청-응답 기능 구현 (12 files, +237/-10)
  - 3c6d354 (2024-12-15): FirebaseConfig 추가 (1 file, +33)
  - 53af568 (2024-12-15): Firebase로 파일 관리 전환 (4 files, +185/-63)
  - 9373627 (2024-11-24): GitHub Actions `docker-image.yml` 생성 (+86)
  - 36f8d22 (2024-11-24): LoggingAspect 추가 (+78)
  - 1e3ae35 (2024-11-24): Prometheus 설정 추가 (+18/-1)
  - c9896d3 (2024-11-13): Report 결과 저장 시 필요한 값만 파싱 저장 (+159/-6)
  - 0c895c3 (2024-11-12): 개인 신청 좌담회 목록 조회 (+56/-10)
  - 242acfd/7f408b6 (2024-11-12): 기업 생성 좌담회 목록 조회 API/기능 추가 (+18/-3, +7)
  - 419aea8/419ecfe (2024-12-12): 기업 전체 광고 조회 API/로직 추가 (+9; +8/-1)
  - 65a2ca6 (2024-11-24): Dockerfile 수정 등 배포 관련 (+25/-4)

- **기능 단위 소유 영역 요약**
  - 설문/좌담회: 질문/답변/좌담회 엔티티·API, 카테고리/신청 목록/상태 처리
  - 보고서: Report 엔티티·리포지토리·서비스, AI 결과 파싱/저장/조회
  - 광고: AI 연동 생성/미리보기/영상 업로드, S3 경로 규칙, 기업별 조회 API
  - 즐겨찾기: 토글/조회/프로필 반환, 보안 설정 연계
  - 포인트: 트랜잭션 엔티티/컨트롤러/서비스 기초 기능
  - 공간(BrandSpace): MongoDB 문서 관리, 기업별 조회/저장 [추정 일부]
  - 공통/플랫폼: 보안/Swagger/CORS/Feign/AWS S3/Firebase 설정, 로깅/모니터링, Docker/배포 파이프라인

## 6) 빌드/설정/DB 근거 파일
- 빌드 파일: `build.gradle`
- 환경설정: `src/main/resources/application.yml`, Logback/Logstash XML
- DB 스키마/매퍼: 전용 마이그레이션/매퍼 파일 없음(엔티티 기반 자동 생성) [추정]

## 7) 실행 요약
- 필수 환경변수 설정 후 `./gradlew bootRun` 또는 Docker로 실행
- 서버 포트: 319
- MySQL과 MongoDB 동시 사용 (JPA + MongoRepository)

## 8) 원시 수치 산출 근거 명령 모음
- 전체 커밋 수: `git rev-list --count HEAD` → 357
- 작성자 커밋 수: `git log --author="lxxsxynnn" --pretty=oneline | wc -l` → 357
- 작성자 수정 파일 수: `git log --author="lxxsxynnn" --name-only --pretty="" | sort -u | wc -l` → 247
- LOC 총합: `git ls-files -z | xargs -0 wc -l | tail -n1` → 5701
- 테이블 수: ripgrep 패턴 `@Entity`로 산정 → 10
- Controller/Service/Repository/DTO 수: 해당 패턴 검색 결과로 산정

비고
- 매퍼/마이그레이션 부재로 DB 스키마는 엔티티 선언과 JPA 설정을 기준으로 기술 (일부는 “추정”).
- 파일/LOC/커밋 통계는 현재 체크아웃 기준이며, 이진/설정/생성물 일부 포함 가능.
