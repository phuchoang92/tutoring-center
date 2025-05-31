package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.AttendanceRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.Attendance;
import com.phuc.tutoring_center.core.domain.enums.AttendanceStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AttendanceService {
    Attendance markAttendance(AttendanceRequestDTO request);
    
    Attendance updateAttendance(UUID id, AttendanceRequestDTO request);
    
    void deleteAttendance(UUID id);
    
    Attendance getAttendance(UUID id);
    
    List<Attendance> getAttendanceByClass(String classId);
    
    List<Attendance> getAttendanceByStudent(String studentId);
    
    List<Attendance> getAttendanceByClassAndDateRange(
        String classId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    List<Attendance> getAttendanceByStudentAndDateRange(
        String studentId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    List<Attendance> getAttendanceByClassAndDate(String classId, LocalDateTime date);
    
    Attendance getAttendanceByClassAndStudentAndDate(
        String classId,
        String studentId,
        LocalDateTime date
    );

    // New utility methods
    Map<AttendanceStatus, Long> getAttendanceStatisticsByClass(String classId);
    
    Map<AttendanceStatus, Long> getAttendanceStatisticsByStudent(String studentId);
    
    Map<AttendanceStatus, Long> getAttendanceStatisticsByClassAndDateRange(
        String classId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    double getAttendanceRateByClass(String classId);
    
    double getAttendanceRateByStudent(String studentId);
    
    double getAttendanceRateByClassAndDateRange(
        String classId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
} 