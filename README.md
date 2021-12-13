# junit-for-dummies
> [더 자바, 애플리케이션을 테스트하는 다양한 방법](https://www.inflearn.com/course/the-java-application-test) 학습을 하면서 정리 및 실습 내용을 작성합니다
> 스프링 2.2+ 버전으로 스프링 부트를 만드는 경우 JUnit5 의존성이 자동으로 추가됩니다.
> JUnit5는 JUnit4 코드에 대해 하위호환성을 제공하므로, 기존 코드 마이그레이션은 굳이 필요 없습니다

### JUnit5 소개
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

### JUnit5 테스트 이름 표기하기
* @DisplayNameGeneration : 언더바를 공백으로 일괄 치환해주는 전략 등을 선택할 수 있습니다
* @DisplayName : 한글 뿐만 아니라 이모지(🎉)도 추가할 수 있습니다
