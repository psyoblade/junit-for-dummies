package me.suhyuk.mock;

import java.util.Optional;

public class MemberServiceV1 implements MemberService {
    @Override
    public void validate(Long memberId) throws InvalidMemberException {
        if (memberId < 0) throw new IllegalArgumentException("멤버 ID는 음수일 수 없습니다");
    }

    @Override
    public Optional<Member> findById(Long memberId) throws MemberNotFoundException {
        return Optional.empty();
    }
}
