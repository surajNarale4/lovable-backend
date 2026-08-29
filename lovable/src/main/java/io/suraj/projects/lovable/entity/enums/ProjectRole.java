package io.suraj.projects.lovable.entity.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.util.Set;

import static io.suraj.projects.lovable.entity.enums.ProjectPermission.*;


@Getter
public enum ProjectRole {
    EDITOR(EDIT,DELETE,VIEW),
    VIEWER(VIEW),
    OWNER(EDIT,DELETE,VIEW,MANAGE_MEMERS,VIEW_MEMBERS);

    ProjectRole(ProjectPermission... projectPermission){
        this.projectPermissions=Set.of(projectPermission);
    }
    private final Set<ProjectPermission> projectPermissions;
}
