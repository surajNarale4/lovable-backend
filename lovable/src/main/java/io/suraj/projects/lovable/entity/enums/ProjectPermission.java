package io.suraj.projects.lovable.entity.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectPermission {

    EDIT("project:edit"),
    VIEW("project:view"),
    DELETE("project:delete"),

    VIEW_MEMBERS("project_member:view"),
    MANAGE_MEMERS("project_member:manage");

    private final String value;

}
