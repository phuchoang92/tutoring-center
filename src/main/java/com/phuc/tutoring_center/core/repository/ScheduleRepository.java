package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByClazz_ClassId(String classId);

    @Query("SELECT s FROM Schedule s WHERE s.dayOfWeek = :dayOfWeek " +
           "AND s.room = :room " +
           "AND ((s.startTime <= :endTime AND s.endTime >= :startTime))")
    List<Schedule> findConflictingSchedules(
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("room") String room,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
} 