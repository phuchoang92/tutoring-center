package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.AssignmentRequestDTO;
import com.phuc.tutoring_center.core.domain.dto.request.AssignmentSubmissionRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.*;
import com.phuc.tutoring_center.core.domain.entity.Class;
import com.phuc.tutoring_center.core.exception.BusinessException;
import com.phuc.tutoring_center.core.repository.*;
import com.phuc.tutoring_center.core.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final AssignmentFileRepository assignmentFileRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    @Transactional
    public Assignment createAssignment(@Valid AssignmentRequestDTO request) {
        log.info("Creating new assignment: {}", request.getName());

        validateAssignmentRequest(request);

        // Find class
        Class clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> {
                    log.warn("Class not found with ID: {}", request.getClassId());
                    return new BusinessException("Class not found", 404, "CLASS_NOT_FOUND");
                });

        // Create assignment
        Assignment assignment = Assignment.builder()
                .id(UUID.randomUUID())
                .clazz(clazz)
                .name(request.getName())
                .subject(request.getSubject())
                .description(request.getDescription())
                .closedTime(request.getClosedTime())
                .dueTime(request.getDueTime())
                .build();

        // Handle file uploads if present
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            List<AssignmentFile> files = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    String filePath = handleFileUpload(file, request.getClassId());
                    AssignmentFile assignmentFile = AssignmentFile.builder()
                            .id(UUID.randomUUID())
                            .assignment(assignment)
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .filePath(filePath)
                            .build();
                    files.add(assignmentFile);
                }
            }
            assignment.setFiles(files);
        }

        Assignment savedAssignment = assignmentRepository.save(assignment);
        log.info("Successfully created assignment with ID: {}", savedAssignment.getId());

        return savedAssignment;
    }

    @Override
    @Transactional
    public Assignment updateAssignment(String id, @Valid AssignmentRequestDTO request) {
        log.info("Updating assignment with ID: {}", id);

        Assignment assignment = getAssignment(id);
        validateAssignmentRequest(request);

        // Update class if changed
        if (!assignment.getClazz().getClassId().equals(request.getClassId())) {
            Class newClass = classRepository.findById(request.getClassId())
                    .orElseThrow(() -> {
                        log.warn("Class not found with ID: {}", request.getClassId());
                        return new BusinessException("Class not found", 404, "CLASS_NOT_FOUND");
                    });
            assignment.setClazz(newClass);
        }

        // Handle file uploads if present
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            // Delete existing files
            for (AssignmentFile file : assignment.getFiles()) {
                deleteFile(file.getFilePath());
            }
            assignment.getFiles().clear();

            // Upload new files
            List<AssignmentFile> files = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    String filePath = handleFileUpload(file, request.getClassId());
                    AssignmentFile assignmentFile = AssignmentFile.builder()
                            .id(UUID.randomUUID())
                            .assignment(assignment)
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .filePath(filePath)
                            .build();
                    files.add(assignmentFile);
                }
            }
            assignment.setFiles(files);
        }

        // Update assignment details
        assignment.setName(request.getName());
        assignment.setSubject(request.getSubject());
        assignment.setDescription(request.getDescription());
        assignment.setClosedTime(request.getClosedTime());
        assignment.setDueTime(request.getDueTime());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        log.info("Successfully updated assignment: {}", id);

        return updatedAssignment;
    }

    @Override
    @Transactional
    public void deleteAssignment(String id) {
        log.info("Deleting assignment with ID: {}", id);

        Assignment assignment = getAssignment(id);

        // Delete all files
        for (AssignmentFile file : assignment.getFiles()) {
            deleteFile(file.getFilePath());
        }

        assignmentRepository.deleteById(UUID.fromString(id));
        log.info("Successfully deleted assignment: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Assignment getAssignment(String id) {
        log.debug("Fetching assignment with ID: {}", id);

        if (!StringUtils.hasText(id)) {
            throw new BusinessException("Assignment ID is required", 400, "INVALID_ID");
        }

        return assignmentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> {
                    log.warn("Assignment not found with ID: {}", id);
                    return new BusinessException("Assignment not found", 404, "ASSIGNMENT_NOT_FOUND");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByClass(String classId) {
        log.debug("Fetching assignments for class: {}", classId);

        if (!StringUtils.hasText(classId)) {
            throw new BusinessException("Class ID is required", 400, "INVALID_CLASS_ID");
        }

        if (!classRepository.existsById(classId)) {
            log.warn("Class not found with ID: {}", classId);
            throw new BusinessException("Class not found", 404, "CLASS_NOT_FOUND");
        }

        List<Assignment> assignments = assignmentRepository.findByClazz_ClassId(classId);
        log.info("Retrieved {} assignments for class: {}", assignments.size(), classId);

        return assignments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByTeacher(String teacherId) {
        log.debug("Fetching assignments for teacher: {}", teacherId);

        if (!StringUtils.hasText(teacherId)) {
            throw new BusinessException("Teacher ID is required", 400, "INVALID_TEACHER_ID");
        }

        List<Assignment> assignments = assignmentRepository.findByTeacherId(UUID.fromString(teacherId));
        log.info("Retrieved {} assignments for teacher: {}", assignments.size(), teacherId);

        return assignments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByStudent(String studentId) {
        log.debug("Fetching assignments for student: {}", studentId);

        if (!StringUtils.hasText(studentId)) {
            throw new BusinessException("Student ID is required", 400, "INVALID_STUDENT_ID");
        }

        List<Assignment> assignments = assignmentRepository.findByStudentId(studentId);
        log.info("Retrieved {} assignments for student: {}", assignments.size(), studentId);

        return assignments;
    }

    @Override
    @Transactional
    public AssignmentSubmission submitAssignment(@Valid AssignmentSubmissionRequestDTO request) {
        log.info("Creating new submission for assignment: {}", request.getAssignmentId());
        
        validateSubmissionRequest(request);
        
        // Find assignment and student
        Assignment assignment = getAssignment(request.getAssignmentId());
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> {
                    log.warn("Student not found with ID: {}", request.getStudentId());
                    return new BusinessException("Student not found", 404, "STUDENT_NOT_FOUND");
                });

        // Check if student is enrolled in the class
        if (!assignment.getClazz().getRegisters().stream()
                .anyMatch(register -> register.getStudent().getStudentId().equals(student.getStudentId()))) {
            throw new BusinessException("Student is not enrolled in this class", 403, "NOT_ENROLLED");
        }

        // Check if assignment is still open
        if (LocalDateTime.now().isAfter(assignment.getClosedTime())) {
            throw new BusinessException("Assignment is closed", 400, "ASSIGNMENT_CLOSED");
        }

        // Create submission
        AssignmentSubmission submission = AssignmentSubmission.builder()
                .id(UUID.randomUUID())
                .assignment(assignment)
                .student(student)
                .submissionTime(LocalDateTime.now())
                .status(LocalDateTime.now().isAfter(assignment.getDueTime()) ? 
                        AssignmentSubmission.SubmissionStatus.LATE : 
                        AssignmentSubmission.SubmissionStatus.SUBMITTED)
                .build();

        // Handle file uploads if present
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            List<SubmissionFile> files = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    String filePath = handleSubmissionFileUpload(file, request.getAssignmentId(), request.getStudentId());
                    SubmissionFile submissionFile = SubmissionFile.builder()
                            .id(UUID.randomUUID())
                            .submission(submission)
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .filePath(filePath)
                            .build();
                    files.add(submissionFile);
                }
            }
            submission.setFiles(files);
        }

        AssignmentSubmission savedSubmission = submissionRepository.save(submission);
        log.info("Successfully created submission with ID: {}", savedSubmission.getId());
        
        return savedSubmission;
    }

    @Override
    @Transactional
    public AssignmentSubmission updateSubmission(String submissionId, @Valid AssignmentSubmissionRequestDTO request) {
        log.info("Updating submission with ID: {}", submissionId);
        
        AssignmentSubmission submission = getSubmission(submissionId);
        validateSubmissionRequest(request);
        
        // Check if assignment is still open
        if (LocalDateTime.now().isAfter(submission.getAssignment().getClosedTime())) {
            throw new BusinessException("Assignment is closed", 400, "ASSIGNMENT_CLOSED");
        }

        // Handle file uploads if present
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            // Delete existing files
            for (SubmissionFile file : submission.getFiles()) {
                deleteFile(file.getFilePath());
            }
            submission.getFiles().clear();

            // Upload new files
            List<SubmissionFile> files = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    String filePath = handleSubmissionFileUpload(file, request.getAssignmentId(), request.getStudentId());
                    SubmissionFile submissionFile = SubmissionFile.builder()
                            .id(UUID.randomUUID())
                            .submission(submission)
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .filePath(filePath)
                            .build();
                    files.add(submissionFile);
                }
            }
            submission.setFiles(files);
        }

        // Update submission time and status
        submission.setSubmissionTime(LocalDateTime.now());
        submission.setStatus(LocalDateTime.now().isAfter(submission.getAssignment().getDueTime()) ? 
                AssignmentSubmission.SubmissionStatus.LATE : 
                AssignmentSubmission.SubmissionStatus.SUBMITTED);

        AssignmentSubmission updatedSubmission = submissionRepository.save(submission);
        log.info("Successfully updated submission: {}", submissionId);
        
        return updatedSubmission;
    }

    @Override
    @Transactional
    public void deleteSubmission(String submissionId) {
        log.info("Deleting submission with ID: {}", submissionId);
        
        AssignmentSubmission submission = getSubmission(submissionId);
        
        // Delete all files
        for (SubmissionFile file : submission.getFiles()) {
            deleteFile(file.getFilePath());
        }

        submissionRepository.deleteById(UUID.fromString(submissionId));
        log.info("Successfully deleted submission: {}", submissionId);
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentSubmission getSubmission(String submissionId) {
        log.debug("Fetching submission with ID: {}", submissionId);
        
        if (!StringUtils.hasText(submissionId)) {
            throw new BusinessException("Submission ID is required", 400, "INVALID_ID");
        }

        return submissionRepository.findById(UUID.fromString(submissionId))
                .orElseThrow(() -> {
                    log.warn("Submission not found with ID: {}", submissionId);
                    return new BusinessException("Submission not found", 404, "SUBMISSION_NOT_FOUND");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentSubmission> getSubmissionsByAssignment(String assignmentId) {
        log.debug("Fetching submissions for assignment: {}", assignmentId);
        
        if (!StringUtils.hasText(assignmentId)) {
            throw new BusinessException("Assignment ID is required", 400, "INVALID_ASSIGNMENT_ID");
        }

        List<AssignmentSubmission> submissions = submissionRepository.findByAssignmentId(UUID.fromString(assignmentId));
        log.info("Retrieved {} submissions for assignment: {}", submissions.size(), assignmentId);
        
        return submissions;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentSubmission> getSubmissionsByStudent(String studentId) {
        log.debug("Fetching submissions for student: {}", studentId);
        
        if (!StringUtils.hasText(studentId)) {
            throw new BusinessException("Student ID is required", 400, "INVALID_STUDENT_ID");
        }

        List<AssignmentSubmission> submissions = submissionRepository.findByStudent_StudentId(studentId);
        log.info("Retrieved {} submissions for student: {}", submissions.size(), studentId);
        
        return submissions;
    }

    @Override
    @Transactional
    public AssignmentSubmission gradeSubmission(String submissionId, Double grade, String feedback) {
        log.info("Grading submission with ID: {}", submissionId);
        
        AssignmentSubmission submission = getSubmission(submissionId);
        
        if (grade != null && (grade < 0 || grade > 100)) {
            throw new BusinessException("Grade must be between 0 and 100", 400, "INVALID_GRADE");
        }

        submission.setGrade(grade);
        submission.setFeedback(feedback);
        submission.setStatus(AssignmentSubmission.SubmissionStatus.GRADED);

        AssignmentSubmission gradedSubmission = submissionRepository.save(submission);
        log.info("Successfully graded submission: {}", submissionId);
        
        return gradedSubmission;
    }

    private String handleFileUpload(MultipartFile file, String classId) {
        try {
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir, classId);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Return relative path
            return Paths.get(classId, newFilename).toString();
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new BusinessException("Failed to upload file", 500, "FILE_UPLOAD_ERROR");
        }
    }

    private void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir, filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage());
            // Don't throw exception as this is a cleanup operation
        }
    }

    private void validateAssignmentRequest(AssignmentRequestDTO request) {
        if (!StringUtils.hasText(request.getName())) {
            throw new BusinessException("Assignment name is required", 400, "INVALID_NAME");
        }

        if (!StringUtils.hasText(request.getSubject())) {
            throw new BusinessException("Subject is required", 400, "INVALID_SUBJECT");
        }

        if (!StringUtils.hasText(request.getDescription())) {
            throw new BusinessException("Description is required", 400, "INVALID_DESCRIPTION");
        }

        if (request.getClosedTime() == null) {
            throw new BusinessException("Closed time is required", 400, "INVALID_CLOSED_TIME");
        }

        if (request.getDueTime() != null && request.getDueTime().isBefore(request.getClosedTime())) {
            throw new BusinessException("Due time must be after closed time", 400, "INVALID_TIME_RANGE");
        }

        if (request.getClosedTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Closed time must be in the future", 400, "INVALID_CLOSED_TIME");
        }

        // Validate files if present
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
                        throw new BusinessException("File size exceeds maximum limit of 10MB", 400, "INVALID_FILE_SIZE");
                    }

                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("application/")) {
                        throw new BusinessException("Invalid file type. Only document files are allowed", 400, "INVALID_FILE_TYPE");
                    }
                }
            }
        }
    }

    private String handleSubmissionFileUpload(MultipartFile file, String assignmentId, String studentId) {
        try {
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir, "submissions", assignmentId, studentId);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Return relative path
            return Paths.get("submissions", assignmentId, studentId, newFilename).toString();
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new BusinessException("Failed to upload file", 500, "FILE_UPLOAD_ERROR");
        }
    }

    private void validateSubmissionRequest(AssignmentSubmissionRequestDTO request) {
        if (!StringUtils.hasText(request.getAssignmentId())) {
            throw new BusinessException("Assignment ID is required", 400, "INVALID_ASSIGNMENT_ID");
        }

        if (!StringUtils.hasText(request.getStudentId())) {
            throw new BusinessException("Student ID is required", 400, "INVALID_STUDENT_ID");
        }

        // Validate files if present
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
                        throw new BusinessException("File size exceeds maximum limit of 10MB", 400, "INVALID_FILE_SIZE");
                    }
                    
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("application/")) {
                        throw new BusinessException("Invalid file type. Only document files are allowed", 400, "INVALID_FILE_TYPE");
                    }
                }
            }
        }
    }
} 