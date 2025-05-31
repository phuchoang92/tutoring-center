package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.ClassRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.Class;
import jakarta.validation.Valid;

public interface ClassService {
    Class createClass(@Valid ClassRequestDTO request);
    Class updateClass(@Valid ClassRequestDTO request);
    void deleteClass(String classId);
}
