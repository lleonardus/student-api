package com.leonardus.student.dto;

import com.leonardus.student.entities.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor @NoArgsConstructor
public class StudentDTO {
    private Long id;

    @NotBlank(message = "Por favor, insira uma matrícula")
    @Length(min = 4, message = "A matrícula deve ter no mínimo {min} caracteres")
    private String matricula;

    @NotBlank(message = "Por favor, insira um nome")
    @Length(min = 4, message = "O nome deve ter no mínimo {min} caracteres")
    private String nome;

    @NotBlank(message = "Por favor, insira um sobrenome")
    @Length(min = 4, message = "O sobrenome deve ter no mínimo {min} caracteres")
    private String sobrenome;

    @Valid
    private Set<Phone> telefones;
}
