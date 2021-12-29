package me.suhyuk.inflearn.study;

import me.suhyuk.inflearn.member.MemberService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test-postgres")
public class StudyServiceContainerTest {

    @Mock
    MemberService memberService;

    @Autowired
    StudyRepository studyRepository;

    @Container // 반드시 static 으로 설정하여 단위 테스트 마다 호출되는 일이 없도록 구성합니다
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6")
            .withDatabaseName("study_db");

    @BeforeEach // 단, 매 호출 직전에 삭제 후에 수행합니다 - 이런 경우 여러 작업 수행 시에 문제는 없는가?
    void beforeEach() {
        studyRepository.deleteAll();
    }

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        System.out.println(postgreSQLContainer.getJdbcUrl());
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @Test
    void testCreateNewStudy() {
        // Given
        assertTrue(true);
        // Set up the system under test

        // When
        // Execute the system under test

        // Then
        // Assert that the expected change has occurred
    }
}
