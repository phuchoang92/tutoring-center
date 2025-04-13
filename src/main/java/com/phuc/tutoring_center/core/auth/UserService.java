package com.phuc.tutoring_center.core.auth;

import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.domain.entity.UserActivity;
import com.phuc.tutoring_center.core.repository.UserActivityRepository;
import com.phuc.tutoring_center.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserActivityRepository userActivityRepository) {
        this.userRepository = userRepository;
        this.userActivityRepository = userActivityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateLastLogin(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null){
            return;
        }
        UserActivity userActivity = UserActivity.builder()
                .id(UUID.randomUUID())
                .user(user)
                .activityTime(LocalDateTime.now())
                .activityType("login")
                .build();
        userActivityRepository.save(userActivity);
    }
}
