package com.example.demo.imageAdapter.repositories;

import com.example.demo.imageAdapter.imageModels.PersonImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonImageRepository extends JpaRepository<PersonImage, Integer> {

}
