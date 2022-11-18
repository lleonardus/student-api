package com.leonardus.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardus.student.dto.StudentDTO;
import com.leonardus.student.entities.Phone;
import com.leonardus.student.entities.Student;
import com.leonardus.student.factory.StudentFactory;
import com.leonardus.student.service.StudentService;
import com.leonardus.student.service.exceptions.DataIntegrityViolationException;
import com.leonardus.student.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

    public static final String EXISTING_MATRICULA = "existing matricula";
    public static final String NON_EXISTING_MATRICULA = "non existing matricula";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StudentService service;

    @MockBean
    ModelMapper mapper;

    Student student;
    StudentDTO studentDTO;

    String json;

    @BeforeEach
    void setUp() throws Exception{
        student = StudentFactory.createStudent();
        studentDTO = StudentFactory.createStudentDTO();

        json = objectMapper.writeValueAsString(studentDTO);

        when(service.findAllStudents()).thenReturn(List.of(student));
        when(service.findStudentByMatricula(EXISTING_MATRICULA)).thenReturn(student);
        when(service.findStudentByMatricula(NON_EXISTING_MATRICULA)).thenThrow(ObjectNotFoundException.class);
        when(service.createStudent(studentDTO)).thenReturn(student);
        when(service.updateStudent(EXISTING_MATRICULA, studentDTO)).thenReturn(student);
        when(service.updateStudent(NON_EXISTING_MATRICULA, studentDTO)).thenThrow(ObjectNotFoundException.class);

        doNothing().when(service).deleteStudentByMatricula(EXISTING_MATRICULA);
        doThrow(ObjectNotFoundException.class).when(service).deleteStudentByMatricula(NON_EXISTING_MATRICULA);

        when(mapper.map(any(), any())).thenReturn(studentDTO);
    }

    @Test
    void findAllStudents_ReturnsAListOfStudentDTO() throws Exception{
        String json = objectMapper.writeValueAsString(List.of(studentDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/estudantes").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findStudentByMatricula_ReturnsAStudentDTO_WhenMatriculaExists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/estudantes/{matricula}", EXISTING_MATRICULA)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findStudentByMatricula_ThrowsObjectNotFoundException_WhenMatriculaDoesNotExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/estudantes/{matricula}", NON_EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createStudent_ReturnsAStudentDTO_WhenSuccessful() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void createStudent_ThrowsDataIntegrityViolationException_WhenMatriculaIsNotUnique() throws Exception{
        when(service.createStudent(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsMethodArgumentNotValidException_WhenMatriculaHasInvalidSize() throws Exception{
        studentDTO.setMatricula("123");
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsMethodArgumentNotValidException_WhenMatriculaIsNull() throws Exception{
        studentDTO.setMatricula(null);
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsMethodArgumentNotValidException_WhenNomeHasInvalidSize() throws Exception{
        studentDTO.setNome("123");
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsMethodArgumentNotValidException_WhenNomeIsNull() throws Exception{
        studentDTO.setNome(null);
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsMethodArgumentNotValidException_WhenSobrenomeHasInvalidSize() throws Exception{
        studentDTO.setSobrenome("123");
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsMethodArgumentNotValidException_WhenSobrenomeIsNull() throws Exception{
        studentDTO.setSobrenome(null);
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsConstraintViolationException_WhenNumeroHasInvalidSize() throws Exception{
        studentDTO.setTelefones(Set.of(new Phone(1L, "")));
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStudent_ThrowsConstraintViolationException_WhenNumeroIsNull() throws Exception{
        studentDTO.setTelefones(Set.of(new Phone(1L, null)));
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/estudantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStudent_ReturnsAStudentDTO_WhenSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateStudent_ThrowsDataIntegrityViolationException_WhenMatriculaIsNotUnique() throws Exception{
        when(service.updateStudent(any(), any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", NON_EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void updateStudent_ThrowsMethodArgumentNotValidException_WhenMatriculaHasInvalidSize() throws Exception{
        studentDTO.setMatricula("123");
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStudent_ThrowsMethodArgumentNotValidException_WhenMatriculaIsNull() throws Exception{
        studentDTO.setMatricula(null);
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStudent_ThrowsMethodArgumentNotValidException_WhenNomeHasInvalidSize() throws Exception{
        studentDTO.setNome("123");
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStudent_ThrowsMethodArgumentNotValidException_WhenNomeIsNull() throws Exception{
        studentDTO.setNome(null);
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStudent_ThrowsMethodArgumentNotValidException_WhenSobrenomeHasInvalidSize() throws Exception{
        studentDTO.setSobrenome("123");
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStudent_ThrowsMethodArgumentNotValidException_WhenSobrenomeIsNull() throws Exception{
        studentDTO.setSobrenome(null);
        json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/estudantes/{matricula}", EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteStudentByMatricula_DoesNothing_WhenMatriculaExists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/estudantes/{matricula}", EXISTING_MATRICULA)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteStudentByMatricula_ThrowsObjectNotFoundException_WhenMatriculaDoesNotExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/estudantes/{matricula}", NON_EXISTING_MATRICULA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}