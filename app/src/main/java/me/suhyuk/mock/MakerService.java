package me.suhyuk.mock;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.suhyuk.junit.Maker;

import java.util.Optional;

@AllArgsConstructor
public class MakerService {
    @NonNull
    private final MemberService memberService;
    @NonNull
    private final MakerRepository repository;

    // Java8: Optional & orElseThrow
    public Maker createNewMaker(Long memberId, Maker maker) throws MemberNotFoundException {
        Optional<Member> member = memberService.findById(memberId);
        Maker newMaker = Maker.builder().name( // !isPresent() 메소드 대신 orElseThrow 통하여 간결하게 작성이 가능합니다
                member.orElseThrow(() -> new IllegalArgumentException("'" + member + "' 멤버가 존재하지 않습니다")).getName()
        ).build();
        return repository.save(newMaker);
    }
}
