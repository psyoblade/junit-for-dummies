# junit-for-dummies
> [더 자바, 애플리케이션을 테스트하는 다양한 방법](https://www.inflearn.com/course/the-java-application-test) 학습을 하면서 정리 및 실습 내용을 작성합니다
> 스프링 2.2+ 버전으로 스프링 부트를 만드는 경우 JUnit5 의존성이 자동으로 추가됩니다.
> JUnit5는 JUnit4 코드에 대해 하위호환성을 제공하므로, 기존 코드 마이그레이션은 굳이 필요 없습니다

## 1. JUnit5 소개
* 컴포넌트 소개
  - Jupiter : Junit 플랫폼이 제공하는 JUnit5 구현체
  - Vintage : JUnit3~4를 지원하는 구현체
  - JUnit Platform : 을 통해서 컴포넌트를 수행하게 됩니다 (IDE)

* 변경 사항
  - JUnit5 부터는 public 함수일 필요가 없다 (Reflection 을 사용하기 때문)
  - SpringBootStarter 를 이용하면 굳이 Jupiter 의존성을 추가해 주지 않아도 됩니다
```ruby
  testImplementation('org.junit.jupiter:junit-jupiter-api:5.5.2')
  testRuntimeOnly('org.junit.jupiter:junit-jupiter-engine:5.5.2')
```

### 1-1. 인텔리제이 단축키
* Shift + Command + T : 클래스의 테스트 케이스 생성
* Shift + Command + I : build.gradle 리프래시
* Ctrl + Option + R : 단위 테스트 실행
* Option + Command + R : 단위 테스트 디버깅

### 1-2. JUnit5 테스트 이름 표기하기
* @DisplayNameGeneration : 언더바를 공백으로 일괄 치환해주는 전략 등을 선택할 수 있습니다
* @DisplayName : 한글 뿐만 아니라 이모지(🎉)도 추가할 수 있습니다


## 2. Assertion 함수 이해하기

### 2-1. Java8 Supplier<String> 활용한 Assertion
```java
  // 메시지가 단순한 경우에는 그냥 String 으로 넘겨도 무방하지만
  assertEquals(StudyStatus.DRAFT, study.getStudyStatus(), "스터디를 처음 만들면 상태값이 DRAFT 입니다");

  // Java8 Supplier<String> 을 받기 때문에 에러 메시지를 만드는 방법이 복잡하다면, Supplier 를 통해 넘기면, 메시지가 필요한 시점에 get 함수가 호출됩니다
  assertEquals(StudyStatus.DRAFT, study.getStudyStatus(), new Supplier<String>() {
    @Override
    public String get() {
      return null;
    }
  });
  
  // 위의 Supplier 구현 대신에 lambda 식을 사용할 수 있습니다
  assertEquals(StudyStatus.DRAFT, study.getStudyStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT 입니다");
```

### 2-2. assertAll 구문을 통한 오류발생한 모든 테스트 결과 확인하기
> 실패한 테스트 하나씩 오류를 알아가면 아주 귀찮을 수 있으며, 실패가 나더라도 모든 테스트를 수행한 결과를 한 번에 볼 수 있습니다 
```java
  assertAll(
    () -> assertNotNull(study),
    () -> assertEquals(StudyStatus.DRAFT, study.getStudyStatus()),
    () -> assertEquals(study.getLimit() > 0, "스터디 참여 인원은 0보다 커야 합니다")
  );
```

### 2-3. assertThrows 통한 예외 처리하기
> 예외를 처리하고 반환값으로 해당 예외의 메시지를 확인할 수 있습니다
```java
  Study.StudyBuilder studyBuilder = Study.builder().studyStatus(StudyStatus.STARTED).limit(101);
  IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> studyBuilder.build());
  assertEquals(Study.ERR_LIMIT, iae.getMessage());
```

### 2-4. assertTimeout 통한 타임 예외 처리
> 정해진 시간 내에 처리되지 않으면 실패하며, Preemptively 경우 도중에 테스트를 종료시킬 수 있습니다
> 단, 스프링 Transaction 경우 ThreadLocal 을 사용하기 때문에, 외부 공유가 불가능한 경우가 있으므로 예상치 못한 상황이 발생할 수 있습니다
```java
  Study study = Study.builder().studyStatus(StudyStatus.DRAFT).limit(10).build();
  assertTimeout(Duration.ofMillis(100), () -> study.sleep(50));
  assertTimeoutPreemptively(Duration.ofMillis(100), () -> study.sleep(5000));
```

### 2-5. assertMatcher 통한 비교
```java
  Study study = Study.builder().studyStatus(StudyStatus.DRAFT).limit(10).build();
  assertThat(study.getLimit()).isGreaterThan(0);
```

## 3