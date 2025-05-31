package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.ClassRequestDTO;
import com.phuc.tutoring_center.core.domain.dto.request.ScheduleRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.Class;
import com.phuc.tutoring_center.core.domain.entity.Schedule;
import com.phuc.tutoring_center.core.domain.entity.Teacher;
import com.phuc.tutoring_center.core.exception.BusinessException;
import com.phuc.tutoring_center.core.repository.ClassRepository;
import com.phuc.tutoring_center.core.repository.ScheduleRepository;
import com.phuc.tutoring_center.core.repository.TeacherRepository;
import com.phuc.tutoring_center.core.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional
    public Class createClass(@Valid ClassRequestDTO request) {
        log.info("Starting class creation with name: {}", request.getClassName());
        
        validateClassRequest(request);
        
        // Find teacher
        Teacher teacher = teacherRepository.findById(UUID.fromString(request.getTeacherId()))
                .orElseThrow(() -> {
                    log.warn("Teacher not found with ID: {}", request.getTeacherId());
                    return new BusinessException("Teacher not found", 404, "TEACHER_NOT_FOUND");
                });

        // Create class
        Class newClass = Class.builder()
                .classId(UUID.randomUUID().toString())
                .teacher(teacher)
                .className(request.getClassName())
                .startDate(parseDate(request.getStartDate()))
                .endDate(parseDate(request.getEndDate()))
                .price(new BigDecimal(request.getPrice()))
                .maxStudents(Integer.parseInt(request.getMaxStudents()))
                .build();

        // Save class first to get the ID
        Class savedClass = classRepository.save(newClass);
        
        // Create and save schedules
        List<Schedule> schedules = createSchedules(savedClass, request.getSchedules());
        scheduleRepository.saveAll(schedules);
        
        log.info("Successfully created class with ID: {} and {} schedules", 
                savedClass.getClassId(), schedules.size());
        
        return savedClass;
    }

    private List<Schedule> createSchedules(Class clazz, List<ScheduleRequestDTO> scheduleRequests) {
        List<Schedule> schedules = new ArrayList<>();
        
        for (ScheduleRequestDTO request : scheduleRequests) {
            validateScheduleRequest(request);
            
            // Check for schedule conflicts
            List<Schedule> conflicts = scheduleRepository.findConflictingSchedules(
                    request.getDayOfWeek(),
                    request.getRoom(),
                    request.getStartTime(),
                    request.getEndTime()
            );
            
            if (!conflicts.isEmpty()) {
                throw new BusinessException(
                        String.format("Schedule conflict found in room %s on %s", 
                                request.getRoom(), request.getDayOfWeek()),
                        400,
                        "SCHEDULE_CONFLICT"
                );
            }
            
            Schedule schedule = Schedule.builder()
                    .clazz(clazz)
                    .dayOfWeek(request.getDayOfWeek())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .room(request.getRoom())
                    .build();
            
            schedules.add(schedule);
        }
        
        return schedules;
    }

    private void validateScheduleRequest(ScheduleRequestDTO request) {
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessException("Start time and end time are required", 400, "INVALID_SCHEDULE_TIME");
        }
        
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException("Start time must be before end time", 400, "INVALID_TIME_RANGE");
        }
        
        if (!StringUtils.hasText(request.getRoom())) {
            throw new BusinessException("Room is required", 400, "INVALID_ROOM");
        }
        
        if (request.getDayOfWeek() == null) {
            throw new BusinessException("Day of week is required", 400, "INVALID_DAY_OF_WEEK");
        }
    }

    @Override
    @Transactional
    public Class updateClass(@Valid ClassRequestDTO request) {
        // TODO: Implement update logic
        throw new BusinessException("Method not implemented", 501, "NOT_IMPLEMENTED");
    }

    @Override
    @Transactional
    public void deleteClass(String classId) {
        log.debug("Deleting class with ID: {}", classId);
        
        if (!StringUtils.hasText(classId)) {
            throw new BusinessException("Class ID is required", 400, "INVALID_ID");
        }

        if (!classRepository.existsById(classId)) {
            log.warn("Class not found with ID: {}", classId);
            throw new BusinessException("Class not found", 404, "CLASS_NOT_FOUND");
        }

        classRepository.deleteById(classId);
        log.info("Successfully deleted class with ID: {}", classId);
    }

    private void validateClassRequest(ClassRequestDTO request) {
        log.debug("Validating class creation request");
        
        if (!StringUtils.hasText(request.getClassName())) {
            throw new BusinessException("Class name is required", 400, "INVALID_CLASS_NAME");
        }

        if (!StringUtils.hasText(request.getTeacherId())) {
            throw new BusinessException("Teacher ID is required", 400, "INVALID_TEACHER_ID");
        }

        if (!StringUtils.hasText(request.getStartDate())) {
            throw new BusinessException("Start date is required", 400, "INVALID_START_DATE");
        }

        if (!StringUtils.hasText(request.getEndDate())) {
            throw new BusinessException("End date is required", 400, "INVALID_END_DATE");
        }

        if (!StringUtils.hasText(request.getPrice())) {
            throw new BusinessException("Price is required", 400, "INVALID_PRICE");
        }

        if (!StringUtils.hasText(request.getMaxStudents())) {
            throw new BusinessException("Maximum students is required", 400, "INVALID_MAX_STUDENTS");
        }

        if (request.getSchedules() == null || request.getSchedules().isEmpty()) {
            throw new BusinessException("At least one schedule is required", 400, "INVALID_SCHEDULES");
        }

        try {
            LocalDate startDate = parseDate(request.getStartDate());
            LocalDate endDate = parseDate(request.getEndDate());
            
            if (endDate.isBefore(startDate)) {
                throw new BusinessException("End date must be after start date", 400, "INVALID_DATE_RANGE");
            }
        } catch (DateTimeParseException e) {
            throw new BusinessException("Invalid date format. Use dd/MM/yyyy", 400, "INVALID_DATE_FORMAT");
        }

        try {
            int maxStudents = Integer.parseInt(request.getMaxStudents());
            if (maxStudents <= 0) {
                throw new BusinessException("Maximum students must be greater than 0", 400, "INVALID_MAX_STUDENTS");
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("Invalid maximum students number", 400, "INVALID_MAX_STUDENTS");
        }

        try {
            BigDecimal price = new BigDecimal(request.getPrice());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Price must be greater than 0", 400, "INVALID_PRICE");
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("Invalid price format", 400, "INVALID_PRICE");
        }
    }

    private LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
}
