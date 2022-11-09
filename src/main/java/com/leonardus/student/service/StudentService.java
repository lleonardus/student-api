package com.leonardus.student.service;

import com.leonardus.student.dto.StudentDTO;
import com.leonardus.student.entities.Student;
import com.leonardus.student.repository.StudentRepository;
import com.leonardus.student.service.exceptions.DataIntegrityViolationException;
import com.leonardus.student.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository repository;

    @Autowired
    ModelMapper mapper;

    public List<Student> findAllStudents(){
        return repository.findAll();
    }

    public Student findStudentByMatricula(String matricula){
        return repository.findByMatricula(matricula)
                .orElseThrow(() -> new ObjectNotFoundException("Não foi possível encontrar o estudante com a matrícula " + matricula));
    }

    public Student createStudent(StudentDTO studentDTO){
        isMatriculaUnique(studentDTO);
        Student student = mapper.map(studentDTO, Student.class);

        return repository.save(student);
    }

    public Student updateStudent(String matricula, StudentDTO studentDTO){
        Student student = this.findStudentByMatricula(matricula);
        studentDTO.setId(student.getId());
        isMatriculaUnique(studentDTO);

        student.setMatricula(studentDTO.getMatricula());
        student.setNome(studentDTO.getNome());
        student.setSobrenome(student.getSobrenome());

        return repository.save(student);
    }

    public void deleteStudentByMatricula(String matricula){
        Student student = this.findStudentByMatricula(matricula);
        repository.delete(student);
    }

    private void isMatriculaUnique(StudentDTO studentDTO){
        Optional<Student> optionalStudent = repository.findByMatricula(studentDTO.getMatricula());

        if (optionalStudent.isPresent() && !optionalStudent.get().getId().equals(studentDTO.getId())){
            throw new DataIntegrityViolationException("A matrícula " + studentDTO.getMatricula() + " já está registrada");
        }
    }
}