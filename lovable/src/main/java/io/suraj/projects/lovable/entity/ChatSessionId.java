package io.suraj.projects.lovable.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ChatSessionId {
    Long projectId;
    Long userId;
    //TODO [Reverse Engineering] generate columns from DB
}