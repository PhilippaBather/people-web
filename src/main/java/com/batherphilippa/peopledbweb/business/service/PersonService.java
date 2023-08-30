package com.batherphilippa.peopledbweb.business.service;

import com.batherphilippa.peopledbweb.business.model.Person;
import com.batherphilippa.peopledbweb.data.FileStorageRepository;
import com.batherphilippa.peopledbweb.data.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final FileStorageRepository fileStorageRepository;

    public PersonService(PersonRepository personRepository, FileStorageRepository fileStorageRepository) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    // record will only be committed to the DB when the transaction within the method
    // is completed successfully
    @Transactional
    public Person save(Person person, InputStream photoImageStream) {
        Person savedPerson = personRepository.save(person);
        fileStorageRepository.save(person.getPhotoFilename(), photoImageStream);
        return savedPerson;
    }
    public Optional<Person> findById(Long aLong) {
        return personRepository.findById(aLong);
    }

    public Iterable<Person> findAllById(Iterable<Long> longs) {
        return personRepository.findAllById(longs);
    }

    public void deleteAllById(Iterable<Long> ids) {
//        Iterable<Person> peopleForDeletion = personRepository.findAllById(ids);
//        Stream<Person> peopleStream = StreamSupport.stream(peopleForDeletion.spliterator(), false);
//        Set<String> filenames = peopleStream
//                .map(Person::getPhotoFilename)
//                .collect(Collectors.toSet());
        // the above using a custom repo method
        Set<String> filenames = personRepository.findFilenamesByIds(ids);
        personRepository.deleteAllById(ids);
        fileStorageRepository.deleteAllByName(filenames);
    }
}
