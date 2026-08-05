package io.suraj.projects.lovable.controller;

import io.suraj.projects.lovable.doto.project.ProjectRequest;
import io.suraj.projects.lovable.doto.project.ProjectResponse;
import io.suraj.projects.lovable.doto.project.ProjectSummeryResponse;
import io.suraj.projects.lovable.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private  ProjectService projectService; //will make final later

    @GetMapping
    public ResponseEntity<List<ProjectSummeryResponse>> getMyProjects(){
        Long userId =1L;
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id){
        Long userId=1L;
        return ResponseEntity.ok(projectService.getUserProjectById(userId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request){
        return ResponseEntity.status(201).body(projectService.createProject(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id , @RequestBody ProjectRequest request){
        Long userId =1L;
        return ResponseEntity.ok(projectService.updateProject(id,request,userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        Long userId = 1L;
        projectService.softDelete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
