package com.example.demo.servises;


import com.example.demo.models.Book;
import com.example.demo.models.Person;
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


    @Transactional
    public void addBookOwner(int bookId, int personId){
        Person person = personService.getCurrentUser();
        Optional<Book> addedBook = bookRepo.findById(bookId);

        if (addedBook.isPresent() && addedBook.get().isAccess()){
            Book currentBook = addedBook.get();
            person.addBook(addedBook.get());

            currentBook.setOwner(person);
            currentBook.setAccess(false);
            currentBook.setTaken_data(new Date(System.currentTimeMillis()));

            bookRepo.save(addedBook.get());
            personService.save(person);
        }


    }



    public List<Book> findAllWithFilter(String q){
        List<Book> finalList = new ArrayList<>();
        List<Book> untreatedList = bookRepo.findByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(q, q);
        log.info("Количество книг до сортировки: {}", untreatedList.size());
        finalList = untreatedList;
//        log.warn("Количество книг после первого цикла сортировки: {}", finalList.size());


        /////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////На этом моменте мы закончили с сортировкой всех книг по нужным нам фильтрам///////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        return finalList;
    }
}
