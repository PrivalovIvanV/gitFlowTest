package com.example.demo.servises;




import com.example.demo.models.Book;
import com.example.demo.repositories.BookRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final JdbcTemplate jdbcTemplate;
    private final BookRepo bookRepo;
    private final PersonService personService;

    public List<Book> findAll(){ return bookRepo.findAll();}
    public List<Book> findAll(String q){ return bookRepo.findByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(q, q);}
    public List<Book> findAll(String q, int page){



        return bookRepo.findAllByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(q, q, PageRequest.of(page, 15)).getContent();
    }
    public List<Book> findAllForPerson(int id){
        int idCurrentUser = id;
        return jdbcTemplate.query("select * from book where person_id = ?",
                new Object[]{idCurrentUser},
                new BeanPropertyRowMapper<>(Book.class));

    }
    public Optional<Book> findById(int id){ return bookRepo.findById(id);}

    public void save(Book book){bookRepo.save(book);}



}
