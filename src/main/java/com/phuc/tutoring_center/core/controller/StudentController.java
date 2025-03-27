package com.phuc.tutoring_center.core.controller;

import com.phuc.tutoring_center.core.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudentController {

    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerStudent(StudentRegisterDTO registerDTO){
        return ResponseEntity.ok(studentService.registerStudent(registerDTO));
    }
}
