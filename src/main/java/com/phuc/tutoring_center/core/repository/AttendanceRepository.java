package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findByClazz_ClassId(String classId);
    
    List<Attendance> findByStudent_StudentId(String studentId);
    
    List<Attendance> findByClazz_ClassIdAndDateBetween(
        String classId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
    
    List<Attendance> findByStudent_StudentIdAndDateBetween(
        String studentId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
    
    @Query("SELECT a FROM Attendance a WHERE a.clazz.classId = :classId AND a.date = :date")
    List<Attendance> findByClassIdAndDate(
        @Param("classId") String classId,
        @Param("date") LocalDateTime date
    );
    
    @Query("SELECT a FROM Attendance a WHERE a.clazz.classId = :classId AND a.student.studentId = :studentId AND a.date = :date")
    Attendance findByClassIdAndStudentIdAndDate(
        @Param("classId") String classId,
        @Param("studentId") String studentId,
        @Param("date") LocalDateTime date
    );
} 