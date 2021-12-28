package me.suhyuk.inflearn.member;

import me.suhyuk.inflearn.domain.Member;
import me.suhyuk.inflearn.domain.Study;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);
    void notify(Study study);
}
