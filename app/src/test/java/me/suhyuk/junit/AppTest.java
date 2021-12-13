/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package me.suhyuk.junit;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // Underscore 를 빈 공백으로 치환
class AppTest {
    @BeforeAll
    static void setUp() { // 반환값이 없는 static 함수로 생성되어야만 합니다
        System.out.println("@BeforeAll");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("@AfterAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("@BeforeEach");
    }

    @AfterEach
    void afterEach() {
        System.out.println("@AfterEach");
    }

    /**
     * https://github.com/Coding/emoji-java
     */
    @Test @DisplayName("인사 함수 \uD83C\uDF89") void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
        System.out.println("appHasAGreeting");
    }

    @Test void test_under_score_name() {
        assertTrue(true);
    }

    @Test @Disabled void disabled() {
        assertTrue(true);
    }

}
