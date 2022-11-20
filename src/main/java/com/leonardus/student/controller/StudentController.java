package com.leonardus.student.controller;

import com.leonardus.student.dtos.StudentDTO;
import com.leonardus.student.entities.Student;
import com.leonardus.student.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/estudantes")
public class StudentController {
    public static final String MATRICULA = "/{matricula}";

    @Autowired
    StudentService service;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<StudentDTO>> findAllStudents(){
        List<Student> students = service.findAllStudents();
        return ResponseEntity.ok().body(students.stream().map(student -> mapper.map(student, StudentDTO.class)).toList());
    }

    @GetMapping(MATRICULA)
    public ResponseEntity<StudentDTO> findStudentByMatricula(@PathVariable String matricula){
        Student student = service.findStudentByMatricula(matricula);
        return ResponseEntity.ok().body(mapper.map(student, StudentDTO.class));
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO){
        Student student = service.createStudent(studentDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(MATRICULA).buildAndExpand(student.getMatricula()).toUri();

        return ResponseEntity.created(uri).body(mapper.map(student, StudentDTO.class));
    }

    @PutMapping(MATRICULA)
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable String matricula, @Valid @RequestBody StudentDTO studentDTO){
        Student student = service.updateStudent(matricula, studentDTO);
        return ResponseEntity.ok().body(mapper.map(student, StudentDTO.class));
    }

    @DeleteMapping(MATRICULA)
    public ResponseEntity<StudentDTO> deleteStudentByMatricula(@PathVariable String matricula){
        service.deleteStudentByMatricula(matricula);
        return ResponseEntity.noContent().build();
    }
}
