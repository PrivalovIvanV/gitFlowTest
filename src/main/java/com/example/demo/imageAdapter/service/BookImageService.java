package com.example.demo.imageAdapter.service;


import com.example.demo.imageAdapter.imageModels.BookImage;
import com.example.demo.imageAdapter.repositories.BookImageRepository;
import com.example.demo.models.Book;
import com.example.demo.servises.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookImageService {

    private final BookImageRepository bookRepository;
    private final JdbcTemplate jdbcTemplate;
    private final BookService bookService;



    @Transactional
    public void saveBookImage(BookImage bookImage, int book_id){
        Optional<Book> optionalBook = bookService.findById(book_id);
        Book originalBook;
        if (optionalBook.isPresent()){
            originalBook = optionalBook.get();
            originalBook.setBookImage(bookImage);
            bookImage.setBook(originalBook);
            bookService.save(originalBook);
        }
        bookRepository.save(bookImage);}


    public BookImage getImageByBookId(int book_id){
        BookImage image = jdbcTemplate.query("select * from book_images where book_id = ?",
                new Object[]{book_id},
                new BeanPropertyRowMapper<>(BookImage.class)
        ).stream().findAny().orElse(null);
        return image;
    }
}
