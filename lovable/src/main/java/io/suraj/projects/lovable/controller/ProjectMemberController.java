package io.suraj.projects.lovable.controller;


import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.service.ProjectMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<ProjectMember>> getAllProjectMembers(@PathVariable Long projectId){
        Long userId= 1L;
        return ResponseEntity.ok(projectMemberService.getAllProjectMembers(userId , projectId));
    }
    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(
            @PathVariable String projectId,
            @RequestBody InviteMemberRequest request
    ){
        Long userId= 1L;
        return ResponseEntity.ok(projectMemberService.inviteMember(userId, projectId, request));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @RequestBody InviteMemberRequest request
    ){
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId,memberId,request,userId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberResponse> deleteMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    ) {
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.deleteProjectMember(projectId, memberId, userId));
    }


}
