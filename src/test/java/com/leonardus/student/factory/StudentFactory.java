package com.leonardus.student.factory;

import com.leonardus.student.dto.StudentDTO;
import com.leonardus.student.entities.Phone;
import com.leonardus.student.entities.Student;
import org.modelmapper.ModelMapper;

import java.util.Set;

public class StudentFactory {

    public static Student createStudent(){
        Phone telefone = new Phone(1L, "4002-8922");
        return new Student(1L, "12345-A", "Leonardo", "Fernandes",
                Set.of(telefone));
    }

    public static StudentDTO createStudentDTO(){
        return new ModelMapper().map(createStudent(), StudentDTO.class);
    }
}
