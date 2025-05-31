package com.phuc.tutoring_center.core.controller;

import com.phuc.tutoring_center.core.domain.dto.request.ClassRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.Class;
import com.phuc.tutoring_center.core.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/class")
@RequiredArgsConstructor
public class ClassController {
    private final ClassService classService;

    @PostMapping("/create")
    public ResponseEntity<Class> createClass(@Valid @RequestBody ClassRequestDTO request) {
        log.debug("Received class creation request: {}", request.getClassName());
        Class createdClass = classService.createClass(request);
        return ResponseEntity.ok(createdClass);
    }

    @PutMapping("/update")
    public ResponseEntity<Class> updateClass(@Valid @RequestBody ClassRequestDTO request) {
        log.debug("Received class update request: {}", request.getClassName());
        Class updatedClass = classService.updateClass(request);
        return ResponseEntity.ok(updatedClass);
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Void> deleteClass(@PathVariable String classId) {
        log.debug("Received class deletion request for ID: {}", classId);
        classService.deleteClass(classId);
        return ResponseEntity.noContent().build();
    }
}
