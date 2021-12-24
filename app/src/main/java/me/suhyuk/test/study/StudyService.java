package me.suhyuk.test.study;

import me.suhyuk.test.domain.Member;
import me.suhyuk.test.domain.Study;

public interface StudyService {
    void validate();
    Study createNewStudy(int numMembers, Member leader);
}
