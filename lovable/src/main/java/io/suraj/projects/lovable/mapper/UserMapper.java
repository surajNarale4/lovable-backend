package io.suraj.projects.lovable.mapper;

import io.suraj.projects.lovable.dto.auth.UserProfileRespose;
import io.suraj.projects.lovable.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileRespose userToUserProfile(User user);
}
