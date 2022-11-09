package com.leonardus.student.service;

import com.leonardus.student.dto.StudentDTO;
import com.leonardus.student.entities.Student;
import com.leonardus.student.factory.StudentFactory;
import com.leonardus.student.repository.StudentRepository;
import com.leonardus.student.service.exceptions.DataIntegrityViolationException;
import com.leonardus.student.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class StudentServiceTest {

    public static final String EXISTING_MATRICULA = StudentFactory.createStudent().getMatricula();
    public static final String NON_EXISTING_MATRICULA = "nonExistingMatricula";
    public static final String MATRICULA_REGISTERED_BY_ANOTHER_STUDENT = "matricula already registered";

    @InjectMocks
    StudentService service;

    @Mock
    StudentRepository repository;

    @Mock
    ModelMapper mapper;

    Student student;
    StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        student = StudentFactory.createStudent();
        studentDTO = StudentFactory.createStudentDTO();

        when(repository.findAll()).thenReturn(List.of(student));
        when(repository.findByMatricula(EXISTING_MATRICULA)).thenReturn(Optional.of(student));
        when(repository.findByMatricula(NON_EXISTING_MATRICULA)).thenReturn(Optional.empty());
        when(repository.save(student)).thenReturn(student);

        when(mapper.map(any(), any())).thenReturn(student);

        doNothing().when(repository).delete(student);
    }

    @Test
    void findAllStudents_ReturnsAListOfStudents() {
        List<Student> response = service.findAllStudents();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(student, response.get(0));
    }

    @Test
    void findStudentByMatricula_ReturnsAStudent_WhenMatriculaExists() {
        Student response = service.findStudentByMatricula(EXISTING_MATRICULA);

        assertNotNull(response);
        assertEquals(student, response);
    }

    @Test
    void findStudentByMatricula_ThrowsObjectNotFoundException_WhenMatriculaDoesNotExist() {
        assertThrows(ObjectNotFoundException.class, () -> service.findStudentByMatricula(NON_EXISTING_MATRICULA));
    }

    @Test
    void createStudent_ReturnsAStudent_WhenMatriculaIsUnique() {
        Student response = service.createStudent(studentDTO);

        assertNotNull(response);
        assertEquals(student, response);
    }

    @Test
    void createStudent_ThrowsDataIntegrityViolationException_WhenMatriculaIsNotUnique() {
        student.setId(student.getId() + 1);
        assertThrows(DataIntegrityViolationException.class, () -> service.createStudent(studentDTO));
    }

    @Test
    void updateStudent_ReturnsAStudent_WhenSuccessful() {
        studentDTO.setMatricula("updated matricula");
        studentDTO.setNome("updated nome");
        studentDTO.setSobrenome("updated sobrenome");

        Student response = service.updateStudent(EXISTING_MATRICULA, studentDTO);

        assertNotNull(response);
        assertEquals(studentDTO.getMatricula(), response.getMatricula());
        assertEquals(studentDTO.getNome(), response.getNome());
        assertEquals(studentDTO.getSobrenome(), response.getSobrenome());
    }

    @Test
    void updateStudent_ThrowsObjectNotFoundException_WhenMatriculaDoesNotExist(){
        assertThrows(ObjectNotFoundException.class, () -> service.updateStudent(NON_EXISTING_MATRICULA, studentDTO));
    }

    @Test
    void updateStudent_ThrowsDataIntegrityViolationException_WhenMatriculaIsNotUnique() {
        Student anotherStudent = StudentFactory.createStudent();
        anotherStudent.setMatricula(MATRICULA_REGISTERED_BY_ANOTHER_STUDENT);
        anotherStudent.setId(studentDTO.getId() + 1);
        studentDTO.setMatricula(MATRICULA_REGISTERED_BY_ANOTHER_STUDENT);

        when(repository.findByMatricula(anotherStudent.getMatricula())).thenReturn(Optional.of(anotherStudent));

        assertThrows(DataIntegrityViolationException.class, () -> service.updateStudent(EXISTING_MATRICULA, studentDTO));
    }

    @Test
    void deleteStudentByMatricula_DoesNothing_WhenMatriculaExists() {
        service.deleteStudentByMatricula(EXISTING_MATRICULA);
        verify(repository, times(1)).delete(student);
    }

    @Test
    void deleteStudentByMatricula_ThrowsObjectNotFoundException_WhenMatriculaDoesNotExist() {
        assertThrows(ObjectNotFoundException.class, () -> service.deleteStudentByMatricula(NON_EXISTING_MATRICULA));
    }
}