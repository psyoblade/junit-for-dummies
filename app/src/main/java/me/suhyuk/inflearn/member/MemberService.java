package me.suhyuk.test.member;

import me.suhyuk.test.domain.Member;
import me.suhyuk.test.domain.Study;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);
    void notify(Study study);
}
