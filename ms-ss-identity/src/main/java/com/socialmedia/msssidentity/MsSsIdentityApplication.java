package com.socialmedia.msssidentity;

import com.socialmedia.msssidentity.model.Credential;
import com.socialmedia.msssidentity.repository.CredentialRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = "com.socialmedia.msssidentity")
public class MsSsIdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSsIdentityApplication.class, args);
	}

	@Bean
	CommandLineRunner init(CredentialRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByEmail("test@example.com").isEmpty()) {
				Credential c = new Credential();
				c.setEmail("test@example.com");
				c.setPassword(encoder.encode("password")); // now using bcrypt!
				repo.save(c);
				System.out.println("📦 Inserted dummy user: test@example.com");
			}
		};
	}

}
