package com.leonardus.student.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PhoneDTO {
    private Long id;
    @NotBlank(message = "Por favor, insira um telefone")
    private String numero;
}
