package me.suhyuk.inflearn.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Study {
    @Id
    @GeneratedValue
    private Long id;
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
