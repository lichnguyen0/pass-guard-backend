package com.passguard.config;

import com.passguard.entity.User;
import com.passguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@gmail.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .email(adminEmail)
                    .passwordHash("dummy_hash_no_security") // Không dùng login nữa
                    .build();
            userRepository.save(admin);
            System.out.println("Default admin user created: admin@gmail.com");
        }
    }
}
