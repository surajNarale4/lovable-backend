package io.suraj.projects.lovable.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionId {
    Long projectId;
    String userId;




    //TODO [Reverse Engineering] generate columns from DB
}