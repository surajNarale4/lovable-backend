package io.suraj.projects.lovable.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Column(unique = true)
    private String StripePriceId;

    private Integer maxProjects;
    private Integer maxTokensPerDay;
    private Integer maxPreviews;
    private Boolean unlimitedAi;

    private Boolean active;
}
