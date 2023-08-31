package com.batherphilippa.peopledbweb.web.controller;

import com.batherphilippa.peopledbweb.business.model.Person;
import com.batherphilippa.peopledbweb.business.service.PersonService;
import com.batherphilippa.peopledbweb.data.FileStorageRepository;
import com.batherphilippa.peopledbweb.data.PersonRepository;
import com.batherphilippa.peopledbweb.exception.StorageException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {

    private final String DISPOSITION = """
                 attachment; filename="%s
                """;

    private final PersonRepository personRepository;
    private final FileStorageRepository fileStorageRepository;
    private final PersonService personService;
    private String disposition;

    public PeopleController(PersonRepository personRepository, FileStorageRepository fileStorageRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.personService = personService;
    }

    @ModelAttribute("people")
    public Page<Person> getPeople(@PageableDefault(size=10) Pageable page) {
        return personService.findAll(page);
    }

    // key not specified, as Spring will accept return type as key
    // this key will be used in the people.html when referencing the object person
    @ModelAttribute
    public Person getPerson() {
        return new Person();
    }
    @GetMapping()
    public String showPeoplePage() {
        return "people";
    }

    // Response Entity is a generic type indicating a resource is to be returned
    @GetMapping("images/{resource}")
    public ResponseEntity<Resource> getImageResource(@PathVariable String resource) {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(DISPOSITION, resource))
                .body(fileStorageRepository.findByName(resource));
    }

    @PostMapping()
    public String savePerson(Model model, @Valid Person person, Errors errors, @RequestParam("photoFilename") MultipartFile photoFile) throws IOException {
        log.info(errors.getAllErrors());
        if (!errors.hasErrors()) {
            try {
                personService.save(person, photoFile.getInputStream());
                return "redirect:people";
            } catch (StorageException e) {
                model.addAttribute("errorMsg", "System is currently unable to accept photo files at this time.");
                return "people";
            }
        }
        return "people";
    }

    @PostMapping(params = "action=delete")
    public String deletePeople(@RequestParam Optional<List<Long>> selections) {
        if (selections.isPresent()) {
            // get unwraps the Optional and returns the List of Longs
            personService.deleteAllById(selections.get());
        }
        return "redirect:people";
    }

    @PostMapping(params = "action=edit")
    public String editPerson(@RequestParam Optional<List<Long>> selections, Model model) {
        log.info(selections);
        if (selections.isPresent()) {
            // first get unwraps the Optional, returns a List; the second gets the first element in the List
            Optional<Person> person = personRepository.findById(selections.get().get(0));
            // bind person to the model
            model.addAttribute("person", person);
        }
        return "people";
    }

    @PostMapping(params = "action=import")
    public String importCSV(@RequestParam("csvFile") MultipartFile csvFile) {
        log.info(csvFile.getOriginalFilename());
        log.info(csvFile.getSize());
        try {
            personService.importCSV(csvFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }
        return "redirect:people";
    }

}
