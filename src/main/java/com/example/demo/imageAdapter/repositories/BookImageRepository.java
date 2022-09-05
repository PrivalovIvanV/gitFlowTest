package com.example.demo.imageAdapter.repositories;

import com.example.demo.imageAdapter.imageModels.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepository extends JpaRepository<BookImage, Integer> {
}
