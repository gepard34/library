package com.nikita.library.controller;

import com.nikita.library.model.Book;
import com.nikita.library.model.User;
import com.nikita.library.service.BookService;
import com.nikita.library.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    List<Book> getAllBooks(){
        return bookService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveBook(@RequestBody Book book) {
        Book savedBook = bookService.save(book);
        return new ResponseEntity<>("The book: " + savedBook.toString() + " was saved", HttpStatus.OK);
    }

    @GetMapping("/take/{id}/{userId}")
    public ResponseEntity<String> takeBook(@PathVariable Integer id, @PathVariable Integer userId) {
        Optional<User> userById = userService.findUserById(userId);
        Optional<Book> byId = bookService.findById(id);

        if (byId.isEmpty()) {
            return new ResponseEntity<>("No such book to take",
                    HttpStatus.BAD_REQUEST);
        }
        Book take = bookService.take(byId.get().getId(), userById.get().getId());
        return new ResponseEntity<>("The book: " + take.toString() + " was taken by " + userById.get().getEmail(), HttpStatus.OK);
    }

    @GetMapping("/back/{id}")
    public ResponseEntity<String> backBook(@PathVariable Integer id) {
        Optional<Book> byId = bookService.findById(id);
        if (byId.isEmpty()) {
            return new ResponseEntity<>("No such book to return",HttpStatus.BAD_REQUEST);
        }
        Book back = bookService.back(byId.get().getId());
        return new ResponseEntity<>("The book: " + back.toString() + " was returned", HttpStatus.OK);
    }
}
