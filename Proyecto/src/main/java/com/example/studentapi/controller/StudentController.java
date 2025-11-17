package com.example.studentapi.controller;

import com.example.studentapi.dto.StudentDTO;
import com.example.studentapi.model.Student;
import com.example.studentapi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service){ this.service = service; }

    @GetMapping
    public List<Student> all(){ return service.findAll(); }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id){ return service.findById(id); }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody StudentDTO dto){
        Student s = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody StudentDTO dto){
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public Student patch(@PathVariable Long id, @RequestBody Map<String,Object> updates){
        return service.patch(id, updates);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
