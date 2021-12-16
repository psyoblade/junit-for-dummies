package me.suhyuk.junit;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public class Study {

    public static final String ERR_LIMIT = "스터디 참여인원은 최대 100명 입니다";

    private StudyStatus studyStatus = StudyStatus.DRAFT;
    private int limit = 10;

    static void validate(int limit) {
        if (limit > 100)
            throw new IllegalArgumentException(ERR_LIMIT);
    }

    public void sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    @Builder
    public Study(StudyStatus studyStatus, int limit) {
        validate(limit);
        this.studyStatus = studyStatus;
        this.limit = limit;
    }

}
