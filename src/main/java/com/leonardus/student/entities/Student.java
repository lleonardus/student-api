package com.leonardus.student.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "estudantes")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String matricula;

    private String nome;

    private String sobrenome;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private Set<Phone> telefones;
}
