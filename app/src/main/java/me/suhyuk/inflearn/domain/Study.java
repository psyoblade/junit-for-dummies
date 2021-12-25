package me.suhyuk.test.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Study {
    private StudyStatus studyStatus;
    private Integer numMembers;
    private String name;
    private LocalDateTime openedDateTime;

    public void open() {
        this.studyStatus = StudyStatus.OPENED;
        this.openedDateTime = LocalDateTime.now();
    }

    @Builder
    public Study(Integer numMembers, StudyStatus studyStatus, String name) {
        this.numMembers = numMembers;
        this.studyStatus = studyStatus;
        this.name = name;
    }
}
