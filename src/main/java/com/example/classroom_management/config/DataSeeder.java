package com.example.classroom_management.config;

import com.example.classroom_management.model.AppUser;
import com.example.classroom_management.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only create if the table is empty
        if (userRepository.count() == 0) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password123")); // Hash it!
            admin.setRole("ADMIN");
            userRepository.save(admin);

            AppUser user = new AppUser();
            user.setUsername("student");
            user.setPassword(passwordEncoder.encode("learn"));
            user.setRole("USER");
            userRepository.save(user);

            System.out.println("✅ Users created!");
        }
    }
}