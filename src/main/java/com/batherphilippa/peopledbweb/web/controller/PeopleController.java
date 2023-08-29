package com.batherphilippa.peopledbweb.web.controller;

import com.batherphilippa.peopledbweb.business.model.Person;
import com.batherphilippa.peopledbweb.data.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private PersonRepository personRepository;

    public PeopleController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @ModelAttribute("people")
    public Iterable<Person> getPeople() {
        return personRepository.findAll();
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

    @PostMapping()
    public String savePerson(@Valid Person person, Errors errors) {
        if (!errors.hasErrors()) {
            personRepository.save(person);
            return "redirect:people";
        }
        return "people";
    }

    @PostMapping(params = "delete=true")
    public String deletePeople(@RequestParam Optional<List<Long>> selections) {
        System.out.println(selections);
        if (selections.isPresent()) {
            // get unwraps the Optional and returns the List of Longs
            personRepository.deleteAllById(selections.get());
        }
        return "redirect:people";
    }

    @PostMapping(params = "edit=true")
    public String editPerson(@RequestParam Optional<List<Long>> selections, Model model) {
        System.out.println(selections);
        if (selections.isPresent()) {
            // first get unwraps the Optional, returns a List; the second gets the first element in the List
            Optional<Person> person = personRepository.findById(selections.get().get(0));
            // bind person to the model
            model.addAttribute("person", person);
        }
        return "people";
    }

}
