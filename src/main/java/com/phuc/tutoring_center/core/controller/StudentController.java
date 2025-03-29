package com.phuc.tutoring_center.core.controller;

import com.phuc.tutoring_center.core.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerStudent(@RequestBody StudentRegisterDTO registerDTO){
        return ResponseEntity.ok(studentService.registerStudent(registerDTO));
    }
}
