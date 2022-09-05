package com.example.demo.repositories;


import com.example.demo.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {

    Optional<Person> findPersonByEmail(String email);
}
