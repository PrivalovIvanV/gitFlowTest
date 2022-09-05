package com.example.demo.controllers;


import com.example.demo.models.Book;
import com.example.demo.models.Person;
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

        if (isPersonAuth()) {
            response = personSer.getCurrentUser().isOwnerThisBook(id);
        }else response = false;
        if (isCatalog == null) {
            isLibrary = false;
        } else isLibrary = true;

        model.addAttribute("isLibrary", isLibrary);
        model.addAttribute("isBookOwnedByCurrentUser", response);
        model.addAttribute("lastSearch", lastSearch);
        model.addAttribute("book", bookResp);
        return "book/BookPage";
    }



    @GetMapping
    public String catalog(Model model){



        List<Integer> pageIterator;
        List<Book> listBook = bookService.findAll(lastSearch, lastPage);

            pageIterator = PageIterator(bookService.findAll(lastSearch));


        model.addAttribute("currentPage", lastPage);
        model.addAttribute("searchVal", lastSearch);
        model.addAttribute("bookList", listBook);
        model.addAttribute("PageIterator", pageIterator);
        return "book/BookCatalog";
    }







    @PostMapping("/{id}")
    public String addBookOwner(@PathVariable("id") int id, Model model){
        if (personSer.isAuth()) {
            log.warn("Попытка добавить книгу");
            bookService.addBookOwner(id, personSer.getCurrentUser().getId());
        }


        model.addAttribute("searchVal", lastSearch);
        model.addAttribute("book", bookService.findById(id).get());
        return "redirect:/catalog/" + id;
    }







    public List<Integer> PageIterator(List<Book> a){
        List<Integer> list = new ArrayList<>();
        log.warn("Длинна листа с книгами составила {}", a.size());
        int numOfPage = a.size()/15;
        if (a.size()%15 != 0) numOfPage++;
        for (int i = 0; i < numOfPage; i++){
            list.add(i);
        }
        return list;
    }//нужно, чтобы можно было по страничкам ходить

    private String lastSearch = "";
    private int lastPage = 0;


    @ModelAttribute(name = "isAuth")
    public boolean isPersonAuth(){ return personSer.isAuth();}

    @ModelAttribute(name = "AuthPerson")
    public Person getAuthPerson(){ return personSer.getCurrentUser();}




}
