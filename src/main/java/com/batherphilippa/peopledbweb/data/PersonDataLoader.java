package com.batherphilippa.peopledbweb.data;

import com.batherphilippa.peopledbweb.business.model.Person;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//@Component
public class PersonDataLoader implements ApplicationRunner {
    private PersonRepository personRepository;

    public PersonDataLoader(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (personRepository.count() == 0) {
            List<Person> people = List.of(new Person(null, "Jake", "Snake", LocalDate.of(1950, 1, 1), "test@test.com", new BigDecimal("35000"), null),
                    new Person(null, "Garcia", "Lorca", LocalDate.of(1975, 6, 4), "test@test.com", new BigDecimal("76000"), null),
                    new Person(null, "Sue", "Sheridan", LocalDate.of(1958, 2, 3), "test@test.com", new BigDecimal("40000"), null),
                    new Person(null, "Dennis", "Menace", LocalDate.of(1965, 6, 2), "test@test.com", new BigDecimal("18000"), null),
                    new Person(null, "Harry", "Maguire", LocalDate.of(1984, 4, 8),"test@test.com", new BigDecimal("42000"), null)
            );
            personRepository.saveAll(people);
        }
    }
}
