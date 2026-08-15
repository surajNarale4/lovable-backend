package io.suraj.projects.lovable.mapper;

import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.entity.ProjectMemberId;
import io.suraj.projects.lovable.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberResponse toMemberMapper(User user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "avtarUrl", source = "user.avtarUrl")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "invitedAt", source = "invitedAt")
    MemberResponse toMemberMapper(ProjectMember projectMember);

    default Long map(ProjectMemberId value){
        return value==null ? null : value.getUserId();
    }

}
