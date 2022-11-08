package com.leonardus.student.dto;

import com.leonardus.student.entities.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor @NoArgsConstructor
public class StudentDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private Set<Phone> telefones;
}
