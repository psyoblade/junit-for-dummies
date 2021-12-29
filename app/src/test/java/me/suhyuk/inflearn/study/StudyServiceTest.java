package me.suhyuk.inflearn.study;

import me.suhyuk.inflearn.domain.Member;
import me.suhyuk.inflearn.domain.Study;
import me.suhyuk.inflearn.domain.StudyStatus;
import me.suhyuk.inflearn.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test-h2")
class StudyServiceTest {

    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @Test
    @DisplayName("스터디를 생성합니다")
    void testCreateNewStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Member member = Member.builder().name("박수혁").memberId(1L).build();
        Study study = Study.builder().studyStatus(StudyStatus.DRAFT).build();

        // When
        when(memberService.findById(anyLong())).thenReturn(Optional.of(member));
        when(studyRepository.save(any(Study.class))).thenReturn(study);
        Study newStudy = studyService.createNewStudy(1L);

        // Then
        assertEquals(study, newStudy);
    }

    @Test
    @DisplayName("다른 사람용자가 볼 수 있도록 스터디를 공개한다")
    void testOpenStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = Study.builder().numMembers(10).name("더 자바, 테스트").build();
        when(studyRepository.save(any(Study.class))).thenReturn(study);

        // When
        Study openedStudy = studyService.openStudy(study);

        // Then
        assertEquals(StudyStatus.OPENED, openedStudy.getStudyStatus());
        assertNotNull(openedStudy.getOpenedDateTime());
        verify(memberService, times(1)).notify(any(Study.class));
    }
}