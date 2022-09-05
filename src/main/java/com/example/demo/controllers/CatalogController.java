package com.example.demo.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {




    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id,
                           Model model){

        return "book/BookPage";
    }



    @GetMapping
    public String catalog(){
        return "book/BookCatalog";
    }





}
