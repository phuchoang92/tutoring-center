package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.ClassRequestDTO;

public interface ClassService {
    Object createClass(ClassRequestDTO request);
    Object updateClass(ClassRequestDTO request);
    Object deleteClass(String classId);
}
