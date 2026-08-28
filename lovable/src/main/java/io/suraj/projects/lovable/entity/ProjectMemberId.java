package io.suraj.projects.lovable.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectMemberId {
    private Long projectId;
    private String userId;
}
