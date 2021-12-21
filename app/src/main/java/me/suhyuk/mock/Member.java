package me.suhyuk.mock;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {
    private String name;
    @Builder
    public Member(String name) {
        this.name = name;
    }
}
