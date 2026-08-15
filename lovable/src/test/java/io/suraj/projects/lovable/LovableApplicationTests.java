package io.suraj.projects.lovable;

import io.suraj.projects.lovable.dto.project.ProjectResponse;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.mapper.ProjectMapper;
import io.suraj.projects.lovable.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LovableApplicationTests {

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private ProjectService projectService;


	@Test
	void contextLoads() {
//		User user = User.builder()
//				.name("suraj")
//				.email("suraj@123")
//				.avtarUrl("/av/cool")
//				.build();
//		Project project = Project.builder()
//				.name("lovable")
//				.owner(user)
//				.build();
//		ProjectResponse projectResponse=projectMapper.toProjectResponse(project);
//		System.out.println(projectResponse);

//		System.out.println(projectService.getUserProjects(1L));


	}

}
