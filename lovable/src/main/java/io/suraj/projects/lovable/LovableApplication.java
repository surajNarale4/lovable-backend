package io.suraj.projects.lovable;

import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LovableApplication implements CommandLineRunner{
	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(LovableApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		if(userRepository.count()==0){
			User user =User.builder()
					.id("1")
					.name("babu")
					.email("babu@41gmail.com")
					.passwordHash("s")

					.build();
			userRepository.save(user);
		}
	}
}
