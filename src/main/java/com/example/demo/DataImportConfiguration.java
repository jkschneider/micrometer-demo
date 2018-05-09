package com.example.demo;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class DataImportConfiguration {
    private static final int POPULATION_SIZE = 1000;

    @Bean
    public CommandLineRunner initData(MongoOperations mongo) {
        return (String... args) -> {
            mongo.dropCollection(Person.class);
            mongo.createCollection(Person.class);
            getPeople().forEach(mongo::save);
        };
    }

    private List<Person> getPeople() {
        List<String> lastNames = loadNames("/dist.all.last");

        List<String> firstNames = loadNames("/dist.female.first");
        firstNames.addAll(loadNames("/dist.male.first"));

        Random randomizer = new Random();

        return IntStream.range(1, POPULATION_SIZE)
            .mapToObj(n -> new Person(UUID.randomUUID().toString(),
                    firstNames.get(randomizer.nextInt(firstNames.size())),
                    lastNames.get(randomizer.nextInt(lastNames.size()))))
            .collect(Collectors.toList());
    }

    private List<String> loadNames(String resource) {
        try {
            return IOUtils.readLines(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8)
                    .stream()
                    .map(l -> l.split("\\s+")[0])
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
