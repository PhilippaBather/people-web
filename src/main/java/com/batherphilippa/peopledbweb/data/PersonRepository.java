package com.batherphilippa.peopledbweb.data;

import com.batherphilippa.peopledbweb.business.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
}
