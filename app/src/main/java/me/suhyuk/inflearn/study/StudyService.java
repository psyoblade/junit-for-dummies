package me.suhyuk.inflearn.study;

import lombok.AllArgsConstructor;
import me.suhyuk.inflearn.domain.Member;
import me.suhyuk.inflearn.domain.Study;
import me.suhyuk.inflearn.domain.StudyStatus;
import me.suhyuk.inflearn.member.MemberService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudyService {

    private MemberService memberService;
    private StudyRepository studyRepository;

    Study createNewStudy(Long memberId) {
        Optional<Member> member = memberService.findById(memberId);
        Member leader = member.orElseThrow(() -> new IllegalArgumentException("멤버 아이디가 존재하지 않습니다 - " + memberId));
        Study study = Study.builder().studyStatus(StudyStatus.DRAFT).name(leader.getName()).build();
        return studyRepository.save(study);
    }

    public Study openStudy(Study study) {
        study.open();
        Study openedStudy = studyRepository.save(study);
        memberService.notify(openedStudy);
        return openedStudy;
    }

}
