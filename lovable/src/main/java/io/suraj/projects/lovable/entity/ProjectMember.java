package io.suraj.projects.lovable.entity;

import io.suraj.projects.lovable.entity.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="project_member")
public class ProjectMember {


    @EmbeddedId
    private ProjectMemberId id;

    @ManyToOne
    @MapsId("projectId")
    private Project project;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;


    private Instant invitedAt;
    private Instant acceptedAt;
}
