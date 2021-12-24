package me.suhyuk.test.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Study {
    private int numMembers;
    private Member leader;
    @Builder
    public Study(int numMembers, Member leader) {
        this.numMembers = numMembers;
        this.leader = leader;
    }
}
