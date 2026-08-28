package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.entity.ProjectMemberId;
import io.suraj.projects.lovable.entity.enums.ProjectRole;
import io.suraj.projects.lovable.error.BadRequestException;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.mapper.MemberMapper;
import io.suraj.projects.lovable.repository.ProjectMemberRepository;
import io.suraj.projects.lovable.repository.ProjectRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.suraj.projects.lovable.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MemberMapper memberMapper;

    /*
    There is unecessary parameter userId , will check later
     */
    @Override
    public List<MemberResponse> getAllProjectMembers(String userId, Long projectId) {
        Project project=projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow(()->new ResourseNotFoundException("no project member found for given user "));
        List<MemberResponse> projectMembers = new ArrayList<>();
        projectMemberRepository.findByProjectId(projectId).stream()
                .map(members-> projectMembers.add(memberMapper.toMemberMapper(members)))
                .toList();
        return projectMembers;


    }


    public MemberResponse inviteMember(String userId, Long projectId, InviteMemberRequest request) {
//         Project project = projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow(()->new ResourseNotFoundException("no project member found for given user "));
//        /*
//         * Users cannot send invitations on behalf of someone else.
//         */
//        if(!project.getOwner().getId().equals(userId)){
//            throw new BadRequestException("This action is not allowed");
//        }
        ProjectMember projectMember =projectMemberRepository.findByProjectIdAndUserIdAndRole(projectId,userId, ProjectRole.OWNER).orElseThrow(
                ()-> new ResourseNotFoundException("User either not have this project or nor owner")
        );
         /*
          * Users cannot send invitations on behalf of someone else.
          */
        if(!projectMember.getUser().getId().equals(userId)) throw new BadRequestException("This Action is not allowed");

        User invitee =userRepository.findById(request.userId()).orElseThrow(
                ()-> new ResourseNotFoundException("this user is no longer available")
        );

        if(request.userId().equals(userId)){
            throw new BadRequestException("cannot invite your self");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());

        ProjectMember inviteProjectMember = ProjectMember.builder()
                .id(projectMemberId)
                .project(projectMember.getProject())
                .user(invitee)
                .role(request.role())
                .invitedAt(Instant.now())
                .build();
        projectMemberRepository.save(inviteProjectMember);
        return memberMapper.toMemberMapper(inviteProjectMember);

    }




    public MemberResponse updateMemberRole(Long projectId, String memberId, InviteMemberRequest request, String userId) {

//        Project project = projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow(()->new ResourseNotFoundException("no project member found for given user "));
//        /*
//         * Users cannot send invitations on behalf of someone else.
//         */
//        if(!project.getOwner().getId().equals(userId)){
//            throw new BadRequestException("This action is not allowed");
//        }
        ProjectMember owner =projectMemberRepository.findByProjectIdAndUserIdAndRole(projectId,userId, ProjectRole.OWNER).orElseThrow(
                ()-> new ResourseNotFoundException("User either not have this project or nor owner")
        );
        /*
         * Users cannot send invitations on behalf of someone else.
         */
        if(owner.getUser().getId().equals(memberId)) throw new BadRequestException("This Action is not allowed");

        User recipient =userRepository.findById(request.userId()).orElseThrow(
                ()-> new ResourseNotFoundException("this user is no longer available")
        );
        ProjectMemberId projectMemberId = ProjectMemberId.builder()
                        .userId(memberId)
                        .projectId(projectId)
                        .build();
        ProjectMember newProjectMember =ProjectMember.builder()
                        .project(owner.getProject())
                        .user(recipient)
                        .role(request.role())
                        .build();

        projectMemberRepository.save(newProjectMember); //saving explicitly as well
       return memberMapper.toMemberMapper(newProjectMember);
    }


    public MemberResponse deleteProjectMember(Long projectId, String memberId, String userId) {
        Project project = projectRepository.findByAccessbileProjects(userId,projectId).orElseThrow(()->new ResourseNotFoundException("no project member found for given user "));
        /*
         * Users cannot send invitations on behalf of someone else.
         */
//        if(!project.getOwner().getId().equals(userId)){
//            throw new BadRequestException("This action is not allowed");
//        }
//        if(project.getOwner().getId().equals(memberId)){
//            throw new BadRequestException("you can't delete yourself using this api");
//        }
        ProjectMemberId projectMemberId=ProjectMemberId.builder()
                .projectId(projectId)
                .userId(memberId)
                .build();
        if(!projectMemberRepository.existsById(projectMemberId))
            throw new ResourseNotFoundException("Project is not exists for given member");
        projectMemberRepository.deleteById(projectMemberId);
        return null;
    }
}
