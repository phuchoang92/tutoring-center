package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;
import com.phuc.tutoring_center.core.domain.entity.Teacher;

import java.util.List;
import java.util.Set;

public interface TeacherService {
    Teacher registerTeacher(TeacherRegisterDTO registerDTO);
    List<Teacher> listTeachers();
    Teacher getTeacherInformation(String id);
    Teacher updateTeacher(String id, TeacherRegisterDTO updateDTO);
    void deleteTeacher(String id);
    Set<Teacher> getTeachersBySubject(String subjectId);
    List<Teacher> getAvailableTeachers(String subjectId, String dayOfWeek, String startTime, String endTime);
} 