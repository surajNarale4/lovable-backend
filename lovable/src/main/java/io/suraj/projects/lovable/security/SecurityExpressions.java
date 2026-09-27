package io.suraj.projects.lovable.security;


import io.suraj.projects.lovable.entity.enums.ProjectPermission;
import io.suraj.projects.lovable.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    public static String getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            log.error("No valid JWT found in security context; principal type: {}",
                    authentication == null ? "null" : authentication.getPrincipal().getClass());
            throw new IllegalStateException("No authenticated JWT principal found");
        }

        String userId = jwt.getSubject();
        log.debug("Resolved user id: {}", userId);
        return userId;
    }

    public boolean hasPermission(Long projectId, ProjectPermission permission){
        String userId= getUserId();
        return projectMemberRepository.findProjectRoleByUseridAndProjectId(userId,Long.valueOf(projectId))
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
