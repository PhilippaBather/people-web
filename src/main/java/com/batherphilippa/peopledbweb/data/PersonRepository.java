package com.batherphilippa.peopledbweb.data;

import com.batherphilippa.peopledbweb.business.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PersonRepository extends CrudRepository<Person,Long>, PagingAndSortingRepository<Person, Long> {

    @Query(nativeQuery = true, value="SELECT PHOTO_FILENAME FROM PERSON WHERE ID IN :IDS")
    public Set<String> findFilenamesByIds(@Param("IDS") Iterable<Long> ids);
}
