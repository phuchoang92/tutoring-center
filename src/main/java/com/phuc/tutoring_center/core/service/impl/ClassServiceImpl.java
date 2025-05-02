package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.ClassRequestDTO;
import com.phuc.tutoring_center.core.repository.ClassRepository;
import com.phuc.tutoring_center.core.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;

    @Override
    public Object createClass(ClassRequestDTO request) {
        return null;
    }

    @Override
    public Object updateClass(ClassRequestDTO request) {
        return null;
    }

    @Override
    public Object deleteClass(String classId) {
        classRepository.deleteById(classId);
        return Strings.EMPTY;
    }
}
