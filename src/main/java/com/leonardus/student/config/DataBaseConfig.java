package com.leonardus.student.config;

import com.leonardus.student.entities.Phone;
import com.leonardus.student.entities.Student;
import com.leonardus.student.repository.StudentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
@Log4j2
public class DataBaseConfig {
    @Bean
    public CommandLineRunner initDataBase(StudentRepository repository){
        Phone p1 = new Phone(null, "4002-8922");
        Phone p2 = new Phone(null, "4002-4002");
        Phone p3 = new Phone(null, "8922-8922");
        Phone p4 = new Phone(null, "1111-8922");
        Phone p5 = new Phone(null, "4002-1111");
        Phone p6 = new Phone(null, "4002-2222");

        Student s1 = new Student(null, "123-A", "Luciana", "Santos", Set.of(p1, p2, p3));
        Student s2 = new Student(null, "123-B", "Antony", "Ferreira", Set.of(p4, p5, p6));
        Student s3 = new Student(null, "123-C", "Sabrina", "Oliveira", Set.of(p6));
        Student s4 = new Student(null, "123-D", "Gabriel", "Silva", null);

        repository.saveAll(List.of(s1, s2, s3, s4));

        return args -> log.info("Banco de dados iniciado com sucesso!");
    }
}
