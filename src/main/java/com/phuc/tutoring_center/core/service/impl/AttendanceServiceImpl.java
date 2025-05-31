package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.AttendanceRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.Attendance;
import com.phuc.tutoring_center.core.domain.entity.Class;
import com.phuc.tutoring_center.core.domain.entity.Student;
import com.phuc.tutoring_center.core.domain.enums.AttendanceStatus;
import com.phuc.tutoring_center.core.repository.AttendanceRepository;
import com.phuc.tutoring_center.core.repository.ClassRepository;
import com.phuc.tutoring_center.core.repository.StudentRepository;
import com.phuc.tutoring_center.core.service.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public Attendance markAttendance(AttendanceRequestDTO request) {
        log.info("Marking attendance for class: {}, student: {}, date: {}", 
            request.getClassId(), request.getStudentId(), request.getDate());

        // Validate class and student
        Class clazz = classRepository.findById(request.getClassId())
            .orElseThrow(() -> new EntityNotFoundException("Class not found"));
        
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // Check if student is enrolled in the class through Register
        boolean isEnrolled = clazz.getRegisters().stream()
            .anyMatch(register -> register.getStudent().getStudentId().equals(student.getStudentId()));
            
        if (!isEnrolled) {
            throw new IllegalArgumentException("Student is not enrolled in this class");
        }

        // Check if attendance already exists for this date
        Attendance existingAttendance = attendanceRepository
            .findByClassIdAndStudentIdAndDate(request.getClassId(), request.getStudentId(), request.getDate());
        
        if (existingAttendance != null) {
            throw new IllegalArgumentException("Attendance already marked for this date");
        }

        // Create new attendance record
        Attendance attendance = Attendance.builder()
            .clazz(clazz)
            .student(student)
            .date(request.getDate())
            .status(request.getStatus())
            .note(request.getNote())
            .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public Attendance updateAttendance(UUID id, AttendanceRequestDTO request) {
        log.info("Updating attendance with id: {}", id);

        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Attendance record not found"));

        // Update fields
        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());

        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public void deleteAttendance(UUID id) {
        log.info("Deleting attendance with id: {}", id);
        
        if (!attendanceRepository.existsById(id)) {
            throw new EntityNotFoundException("Attendance record not found");
        }
        
        attendanceRepository.deleteById(id);
    }

    @Override
    public Attendance getAttendance(UUID id) {
        log.info("Getting attendance with id: {}", id);
        
        return attendanceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Attendance record not found"));
    }

    @Override
    public List<Attendance> getAttendanceByClass(String classId) {
        log.info("Getting attendance records for class: {}", classId);
        
        return attendanceRepository.findByClazz_ClassId(classId);
    }

    @Override
    public List<Attendance> getAttendanceByStudent(String studentId) {
        log.info("Getting attendance records for student: {}", studentId);
        
        return attendanceRepository.findByStudent_StudentId(studentId);
    }

    @Override
    public List<Attendance> getAttendanceByClassAndDateRange(
        String classId,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        log.info("Getting attendance records for class: {} between {} and {}", 
            classId, startDate, endDate);
        
        return attendanceRepository.findByClazz_ClassIdAndDateBetween(classId, startDate, endDate);
    }

    @Override
    public List<Attendance> getAttendanceByStudentAndDateRange(
        String studentId,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        log.info("Getting attendance records for student: {} between {} and {}", 
            studentId, startDate, endDate);
        
        return attendanceRepository.findByStudent_StudentIdAndDateBetween(studentId, startDate, endDate);
    }

    @Override
    public List<Attendance> getAttendanceByClassAndDate(String classId, LocalDateTime date) {
        log.info("Getting attendance records for class: {} on date: {}", classId, date);
        
        return attendanceRepository.findByClassIdAndDate(classId, date);
    }

    @Override
    public Attendance getAttendanceByClassAndStudentAndDate(
        String classId,
        String studentId,
        LocalDateTime date
    ) {
        log.info("Getting attendance record for class: {}, student: {}, date: {}", 
            classId, studentId, date);
        
        return attendanceRepository.findByClassIdAndStudentIdAndDate(classId, studentId, date);
    }

    @Override
    public Map<AttendanceStatus, Long> getAttendanceStatisticsByClass(String classId) {
        return null;
    }

    @Override
    public Map<AttendanceStatus, Long> getAttendanceStatisticsByStudent(String studentId) {
        return null;
    }

    @Override
    public Map<AttendanceStatus, Long> getAttendanceStatisticsByClassAndDateRange(String classId, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public double getAttendanceRateByClass(String classId) {
        return 0;
    }

    @Override
    public double getAttendanceRateByStudent(String studentId) {
        return 0;
    }

    @Override
    public double getAttendanceRateByClassAndDateRange(String classId, LocalDateTime startDate, LocalDateTime endDate) {
        return 0;
    }
} 