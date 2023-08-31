package com.batherphilippa.peopledbweb.business.service;

import com.batherphilippa.peopledbweb.business.model.Person;
import com.batherphilippa.peopledbweb.data.FileStorageRepository;
import com.batherphilippa.peopledbweb.data.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipInputStream;

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

    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
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

    // assumes file streamed is a zip file; doesn't check file extension
    // file read as a byte stream
    // this needs to be read and interpreted as a zip file
    public void importCSV(InputStream csvFileStream) {
        try {
            // think of ZipInputStream as a filter and hose
            // the csvFileStream that coming from it
            ZipInputStream zipINputStream = new ZipInputStream(csvFileStream);
            // jumps to first file within the zip file
            zipINputStream.getNextEntry();
            // stream of bytes to chars
            InputStreamReader inputStreamReader = new InputStreamReader(zipINputStream);
            // read lines of chars
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // returns a stream of strings
            bufferedReader
                    .lines()
                    .skip(1)
                    .limit(20)
                    .map(Person::parse)
                    .forEach(personRepository::save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
