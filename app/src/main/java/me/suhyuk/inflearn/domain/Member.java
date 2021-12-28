package me.suhyuk.inflearn.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long memberId;
    private String name;

    @Builder
    public Member(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }
}
