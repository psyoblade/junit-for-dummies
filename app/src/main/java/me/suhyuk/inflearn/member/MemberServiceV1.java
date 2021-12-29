package me.suhyuk.inflearn.member;

import lombok.AllArgsConstructor;
import me.suhyuk.inflearn.domain.Member;
import me.suhyuk.inflearn.domain.Study;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberServiceV1 implements MemberService {

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.empty();
    }

    @Override
    public void notify(Study study) {

    }
}
