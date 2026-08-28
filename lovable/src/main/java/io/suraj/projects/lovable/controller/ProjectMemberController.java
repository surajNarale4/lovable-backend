package io.suraj.projects.lovable.controller;


import io.suraj.projects.lovable.dto.members.InviteMemberRequest;
import io.suraj.projects.lovable.dto.members.MemberResponse;
import io.suraj.projects.lovable.entity.ProjectMember;
import io.suraj.projects.lovable.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllProjectMembers(@PathVariable Long projectId){
        String userId ="";
        return ResponseEntity.ok(projectMemberService.getAllProjectMembers(userId , projectId));
    }
    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(
            @PathVariable Long projectId,
            @RequestBody @Valid InviteMemberRequest request
    ){
        String userId ="";
        return ResponseEntity.ok(projectMemberService.inviteMember(userId, projectId, request));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable String memberId,
            @RequestBody @Valid InviteMemberRequest request
    ){
        String userId ="";
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId,memberId,request,userId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberResponse> deleteMemberRole(
            @PathVariable Long projectId,
            @PathVariable String memberId
    ) {
        String userId ="";
        return ResponseEntity.ok(projectMemberService.deleteProjectMember(projectId, memberId, userId));
    }


}
