package com.example.demo;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class PersonApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonApplication.class, args);
    }
}

@Data
@Document
class Person {
    @NonNull @Id private String id;
    @NonNull private String firstName;
    @NonNull private String lastName;
}

interface PersonRepository extends Repository<Person, String> {
    Flux<Person> findAll();

    Flux<Person> findByLastNameIgnoringCase(String lastName);
}

@RestController
class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/persons")
    public Flux<Person> all() {
        return this.repository.findAll();
    }

    @GetMapping(path = "/person/{lastName}")
    public Flux<Person> byLastName(@PathVariable String lastName) {
        return this.repository.findByLastNameIgnoringCase(lastName);
    }
}
