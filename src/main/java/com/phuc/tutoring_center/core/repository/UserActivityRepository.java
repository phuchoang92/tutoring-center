package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {

}