package io.suraj.projects.lovable.config;


import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.entity.enums.ProjectPermission;
import io.suraj.projects.lovable.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component("security")
@Slf4j
@RequiredArgsConstructor
public class SecurityExpressions {
    private final ProjectMemberRepository projectMemberRepository;
    /*
    will check below getUserId later
     */
    public static String getUserId(){
        Jwt jwt=(Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("user id ={}",jwt);
        assert jwt != null;
        return jwt.getSubject();

    }

    public boolean hasPermission(Long projectId, ProjectPermission permission){
        String userId= getUserId();
        return projectMemberRepository.findProjectRoleByUseridAndProjectId(userId,projectId)
                .map(role->role.getProjectPermissions().contains(permission))
                .orElse(false);

    }

    public boolean hasEditPermission(Long projectId){
        return hasPermission(projectId,ProjectPermission.EDIT);
    }
    public boolean hasViewPermission(Long projectId){
        return hasPermission(projectId,ProjectPermission.VIEW);
    }
    public boolean hasDeletePermission(Long projectId){
        return hasPermission(projectId,ProjectPermission.DELETE);
    }
    public boolean canViewMembers(Long projectId){
        return hasPermission(projectId,ProjectPermission.VIEW_MEMBERS);
    }
    public boolean canManageMembers(Long projectId){
        return hasPermission(projectId,ProjectPermission.MANAGE_MEMERS);
    }

}
