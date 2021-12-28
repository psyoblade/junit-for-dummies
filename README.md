# junit-for-dummies
> [더 자바, 애플리케이션을 테스트하는 다양한 방법](https://www.inflearn.com/course/the-java-application-test) 학습을 하면서 정리 및 실습 내용을 작성합니다
> 스프링 2.2+ 버전으로 스프링 부트를 만드는 경우 JUnit5 의존성이 자동으로 추가됩니다.
> JUnit5는 JUnit4 코드에 대해 하위호환성을 제공하므로, 기존 코드 마이그레이션은 굳이 필요 없습니다

## 1. JUnit5 소개
* [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
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

### 2-6. assumeTrue 조건에 따른 테스트
```java
  @Test @DisplayName("조건에 따른 테스트 수행") void testAssumeTrue() {
    String debug = System.getenv("DEBUG");
    System.out.println("debug = " + debug);
    assumeTrue("True".equalsIgnoreCase(debug));
    // 아래의 경우는 초기 상태가 DRAFT 이므로 오류가 발생하지만 환경 변수 DEBUG=true 인 경우만 수행됩니다
    Study study = Study.builder().studyStatus(StudyStatus.STARTED).limit(-10).build();
    assertTrue(StudyStatus.DRAFT == study.getStudyStatus());
  }

```

### 2-7. [@Tag](https://junit.org/junit5/docs/current/user-guide/#running-tests-tags) 통하여 수행 조건을 지정하여 수행하는 테스트
```java
  @Tag("fast")
  @Test @DisplayName("태그 fast 작업") void testFast() {
    System.out.println("수행시간이 짧아서 로컬에서 실행하는 테스트");
    assertTrue(true);
  }

  @Tag("slow")
  @Test @DisplayName("태그 slow 작업") void testSlow() {
    System.out.println("수행시간이 길어서 로컬에서 실행하지 않는 테스트");
    assertTrue(true);
  }
```
```yaml
subprojects {
  ...
  test {
    useJUnitPlatform {
      excludeTags 'debug'
    }
  }

  task fastTest(type: Test) {
    useJUnitPlatform {
      includeTags 'fast'
    }
  }

  task slowTest(type: Test) {
    useJUnitPlatform {
      includeTags 'slow'
    }
  }
}
```
```bash
./gradlew clean test
./gradlew clean fastTest
./gradlew clean slowTest
```

### 2-8. 커스텀 어노테이션 태그
> @Test, @Tag("fast") 등의 여러 어노테이션을 메타 어노테이션으로 활용하여 컴포즈 어노테이션을 생성합니다
```java
@Target(ElementType.METHOD) // 어디에 쓸 수 있는가 - 메소드
@Retention(RetentionPolicy.RUNTIME) // 이 어노테이션이 런타임시에도 유지되어야 함
@Test // 테스트 어노테이션으로 사용할 것이며
@Tag("fast") // 메타 태그를 fast 등록합니다
public @interface FastTest { // FastTest 어노테이션은 @Test 와 @Tag 2개를 메타 어노테이션으로 사용하여 컴포즈 어노테이션을 생성할 수 있다
}
```

### 2-9. 테스트 반복하기
> [Repeated Tests](https://www.baeldung.com/junit-5-repeated-test), [Parameterized Tests](https://www.baeldung.com/parameterized-tests-junit-5)
```java
  @DisplayName("파라메터 테스트")
  @ParameterizedTest(name = "[{index}] - {displayName} message = '{0}'")
  @ValueSource(strings = {
        "파라메터를", "직접", "입력하여", "반복할", "수", "있습니다"
  })
  @NullAndEmptySource
  void testRepeatedWithParameters(String word) {
    System.out.println("word = " + word);
    assertTrue(true);
  }
```

### 2-10. 다수의 파라메터와 다수의 인자 테스트

> [Argument Conversion](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-argument-conversion) 참고
```java
  @ParameterizedTest
  @DisplayName("다수의 파라메터와 다수의 인자 테스트")
  @CsvSource({"'박수혁', 10", "'김영미', 20", "'박소원', 30", "'박시훈', 40"})
  void testCsvSourceWithMultipleArguments(String name, Integer age) {
    System.out.println("maker = " + Maker.builder().name(name).age(age).build());
  }
```


## 3. Mockito
> Mock: 진짜 객체와 비슷하게 동작하지만 프로그래머가 그 객체의 행동을 관리하는 객체
> Mockito : Mock 객체를 쉽게 만들고 관리하고 검증할 수 있는 방법을 제공합니다
> 데이터베이스 혹은 외부 API 연동을 하는 경우 내부 구현을 모르고, 단위 테스트 수준에서 연동하는 것은 번거로운 일이므로, 이럴 때에 활용합니다

### 3-1. 단위 테스트
> [UnitTest](https://martinfowler.com/bliki/UnitTest.html) 모든 객체를 Mock 객체로 만들어야 할 필요는 없다.
> 단위를 생각할 때에 메소드 하나가 아니라 행동의 단위를 하나의 기준으로 볼 수도 있다 (동료와 협의를 통해 컨센서스를 맞추면 좋다)
> 특히 외부 서비스 혹은 라이브러리를 이용하는 경우에만 Mock 을 적용하겠다 등으로 정의해도 좋을 것 같다 (Bango.com 같은 테스트 서버도 있다)

* 예를 들어 하나의 컨틀로러와 서비스를 내가 구현했고, 이를 하나의 행위로 본다고 하면, 나머지 객체들은 연관되어 동작하므로, Controller 만 테스트해도 좋다
  - 팀 내에 이러한 기준에 대한 협의가 있어서 기준을 정해두면 좋겠다
  - AdminClient 와 같이 개발 업무 R&R 이 다른 경우라면 별도로 단위 테스트를 가져가는 것이 좋을 수도 있다.

### 3-2. 시작하기
> 기본적으로 spring-boot-starter-test 패키지에 의존성이 포함되어 있으나
> 'org.mockito:mockito-core:3.1.0' : 코어 라이브러리
> 'org.mockito:mockito-junit-jupiter:3.1.0' : JUnit 테스트에서 Mockito 를 사용할 수 있도록 하는 확장 라이브러리

* [Lombok @NonNull](https://projectlombok.org/features/NonNull) 은 `@NotNull`은 Documentation 이며, 하일라이트를 해주는 효과
* Lombok 의 `@NonNull` 을 사용해야 NullPointerException 을 던져줍니다

### 3-3. 목키토가 하는 일을 굳이 내가 해보기
> 아래와 같이 인터페이스만 가진 객체에 대해서 빈 껍질을 구현해서 객체를 만드는 과정을 Mockito 가 대신 해준다고 보면 됩니다

* 현재 모든 서비스는 인터페이스만 존재하기 때문에 모든 구현체가 없다면 수행이 불가능합니다
```java
@Test
@DisplayName("Mockito 가 하는 일을 굳이 내가 해보기")
  void createMakerService() {
    MemberService memberService = new MemberService() {
      @Override
      public void validate(Long memberId) throws InvalidMemberException {
        }
      @Override
      public Optional<Member> findById(Long memberId) throws MemberNotFoundException {
        return Optional.empty();
      }
    };
  }
```

### 3-4. 모키토 mock 함수를 이용한 모키토 객체 생성
```java
    @Test
    @DisplayName("모키토를 써서 객체 생성해보기")
    void testCreateServiceUsingMockito() {
        MemberService memberService = Mockito.mock(MemberService.class);
        MakerRepository makerRepository = Mockito.mock(MakerRepository.class);
        MakerService makerService = new MakerService(memberService, makerRepository);
        assertNotNull(makerService);
    }
```

### 3-5. 모키토 어노테이션을 사용하는 방법 - 클래스 전역적으로 사용하는 경우
> 단순히 @Mock 은 선언만 하지 생성은 해주지 않습니다 - `@ExtendWith(MockitoExtension.class)`
```java
    @Mock MemberService memberService;
    @Mock MakerRepository makerRepository;
    @Test
    @DisplayName("어노테이션을 이용한 모키토")
    void testCreateServiceUsingMockitoAnnotation() {
        MakerService makerService = new MakerService(memberService, makerRepository);
        assertNotNull(makerService);
    }
```

### 3-6. 모키노 인자로 전달받고 싶은 경우
```java
    @Test
    @DisplayName("인자로 어노테이션 객체를 받는 경우")
    void testCreateServiceUsingParameterAnnotation(@Mock MemberService memberService,
                                                   @Mock MakerRepository makerRepository) {
        MakerService makerService = new MakerService(memberService, makerRepository);
        assertNotNull(makerService);
    }
```

### 3-7. 목객체의 동작과 `lenient` 키워드 사용법 
> JUnit5 기본 설정이 엄격한 단위 테스트이며, when...thenReturn 절을 선언하고 사용하지 않으면 [UnnecessaryStubbingException](https://www.baeldung.com/mockito-unnecessary-stubbing-exception) 가 떨어진다
> 하여 반드시 사용하는 행동만 정의하되, 만일 애매하다면 Mockito.lenient() 호출하고, when...thenReturn 절을 사용하면 됩니다

* Mock 객체의 행동 (implements 코드 자동 생성되었을 때의 상태)
  - 일반 타입은 null, Optional 경우는 Optional.empty 반환
  - void 메소드는 예외도 던지지 않고 아무일도 일어나지 않음
  - Primitive 타입은 기본 Primitive 값
  - 콜렉션은 비어있는 콜렉션
```java
    @Test
    @DisplayName("엄격한 vs 유연한 테스트")
    void testLenientAndStrict(@Mock Foo foo) {
        Mockito.lenient()
                .when(foo.getInt())
                .thenReturn(10);
        assertFalse(foo.getBoolean());
        // lenient 경우는 사용하지 않아도 오류가 없으나

        when(foo.getInt()).thenReturn(10);
        assertFalse(foo.getBoolean());
        // when 에서 선언하고 사용하지 않으면 UnnecessaryStubbingException 이 발생하게 됩니다
    }
```

### 3-8. 행동 조작하기 (Stubbing)
```java
    @Test
    @DisplayName("모키토 객체의 행동읠 정의하는 방법")
    void testMockAction() throws MemberNotFoundException {
    Member oldMember = Member.builder().name("예전이름").build();
    Maker oldMaker = Maker.builder().name("박수혁").age(10).build();
    Mockito.lenient() // Loosely stubbing jupiter 이전 버전 stubbing default 값을 사용합니다
    .when(memberService.findById(any(Long.class)))
    .thenReturn(Optional.ofNullable(oldMember));
    Mockito.lenient() // Strict stubbing default 값을 사용합니다
    .when(makerRepository.save(any(Maker.class)))
    .thenReturn(oldMaker);
    MakerService makerService = new MakerService(memberService, makerRepository);
    Long memberId = 1L;
    Maker newMaker = makerService.createNewMaker(memberId, oldMaker);
    assertEquals(oldMaker.getName(), newMaker.getName());
    }
```

### 3-9. 예외를 다루는 방법
```java
    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L})
    @DisplayName("예외를 던지는 모키토")
    void testMockThrowException(long id) throws MemberNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> memberService.findById(id));
    }
```

### 3-10. 인자 매칭을 통한 검증
```java
    @Test
    @DisplayName("아규먼트 매처를 활용한 리스트 갯수 확인")
    void testArgumentMatcher() {
        LinkedList mockedList = Mockito.mock(LinkedList.class);
        when(mockedList.get(anyInt())).thenReturn("first");
        verify(mockedList).add(argThat(someString -> someString.toString().length() > 2));
    }
```

### 3-11. 작업 내역을 검증
```java
    @Test
    @DisplayName("수행한 작업들을 확인합니다")
    void testVerifyMockedList() {
        // 인터페이스가 아니라 실제 객체를 목킹합니다
        List mockedList = mock(List.class);
        // 한 번 아래와 같이 명령이 수행된 것을 기억하고 있다가
        mockedList.add("one");
        mockedList.clear();
        // 수행여부를 확인합니다
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }
```

### 3-12. 호출 횟수를 검증 
```java
    @Test
    @DisplayName("호출 횟수를 검증")
    void testVerifyExactNumberOfInvocations() {
        LinkedList mockedList = mock(LinkedList.class);
        mockedList.add("once");
        mockedList.add("twice");
        mockedList.add("twice");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");
        verify(mockedList, never()).add("never happend");

        verify(mockedList, atMostOnce()).add("once");
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(3)).add("three times");
    }
```

### 3-13. 호출 순서를 검증 
```java
    @Test
    @DisplayName("호출된 메소드/목객체 순서를 검증")
    void testVerifyInOrder() {
        List singleMock = mock(List.class);
        singleMock.add("was added first");
        singleMock.add("was added second");

        InOrder inOrder = inOrder(singleMock);
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        List firstMock = mock(List.class);
        List secondMock = mock(List.class);
        firstMock.add("was called first");
        secondMock.add("was called second");

        InOrder inOrders = inOrder(firstMock, secondMock);
        inOrders.verify(firstMock).add("was called first");
        inOrders.verify(secondMock).add("was called second");
    }
```

### 3-14. 순차적으로 다른 동작을 테스트
```java
    @Test
    @DisplayName("순차적인 반환값 스터빙")
    void testConsecutiveCalls() {
        List mockList = mock(List.class);
        when(mockList.add(0))
                .thenThrow(new IllegalArgumentException(""))
                .thenReturn(true);
        // 처음 호출은 예외를 던지고, 두 번째는 성공하는 스터빙
        assertThrows(IllegalArgumentException.class, () -> mockList.add(0));
        assertTrue(mockList.add(0));
    }
```

## 4. 도커와 테스트
> [테스트 컨테이너](https://www.testcontainers.org)를 이용하여 도커 컨테이너를 단위 테스트에서 활용합니다
> [컨테이너 모듈](https://www.testcontainers.org/modules/docker_compose/) 참고하여 활용
