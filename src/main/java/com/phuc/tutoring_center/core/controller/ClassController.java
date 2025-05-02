package com.phuc.tutoring_center.core.controller;

import com.phuc.tutoring_center.core.domain.dto.request.ClassRequestDTO;
import com.phuc.tutoring_center.core.service.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("class")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClass(ClassRequestDTO request){
        return ResponseEntity.ok(classService.createClass(request));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateClass(ClassRequestDTO request){
        return ResponseEntity.ok(classService.createClass(request));
    }

}
