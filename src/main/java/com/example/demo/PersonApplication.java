package com.example.demo;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Random;

@SpringBootApplication
public class PersonApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonApplication.class, args);
    }
}

@Data
@Document
class Person {
    @NonNull
    @Id
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags() {
        return r -> r.config().commonTags("host", "ecomwassrv1");
    }
}

interface PersonRepository extends Repository<Person, String> {
    Flux<Person> findAll();

    Flux<Person> findByLastNameIgnoringCase(String lastName);
}

@RestController
class PersonController {
    private final PersonRepository repository;
    private final Random r = new Random();

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/persons")
    public Flux<Person> all() {
        if((r.nextGaussian() + 1) / 2 < 0.01) {
            throw new RuntimeException("random failure");
        }
        return this.repository.findAll();
    }

    @GetMapping(path = "/person/{lastName}")
    public Flux<Person> byLastName(@PathVariable String lastName) {
        return this.repository.findByLastNameIgnoringCase(lastName);
    }
}
