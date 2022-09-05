package com.example.demo.controllers;


import com.example.demo.models.Book;
import com.example.demo.servises.BookService;
import com.example.demo.servises.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final BookService bookService;
    private final PersonService personSer;



    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id,
                           @RequestParam( name = "isCatalog", required = false) String isCatalog,
                           Model model){

        boolean isLibrary;
        boolean response;
        Book bookResp =  bookService.findById(id).get();

        model.addAttribute("book", bookResp);
        return "book/BookPage";
    }



    @GetMapping
    public String catalog(Model model){


        List<Book> listBook = bookService.findAll();

        model.addAttribute("bookList", listBook);
        return "book/BookCatalog";
    }


}
