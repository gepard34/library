package com.nikita.library.util;

import com.nikita.library.model.Book;
import com.nikita.library.model.User;
import com.nikita.library.repository.BookRepository;
import com.nikita.library.service.BookService;
import com.nikita.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    private  BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        Book book = Book.builder()
                .author("petrov")
                .year(1999)
                .name("prestuplenie i nakazanie")
                .isAvailable(true)
                .build();
        Book book2 = Book.builder()
                .author("sidorov")
                .year(1998)
                .name("Mu-mu")
                .isAvailable(true)
                .build();
        Book book3 = Book.builder()
                .author("lermontov")
                .year(1997)
                .name("Geroi nashego vremeni")
                .isAvailable(true)
                .build();
        User user = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("fhasjdfh@gmail.com")
                .build();
        User user2 = User.builder()
                .firstName("Petr")
                .lastName("Petrov")
                .email("petrov@gmail.com")
                .build();
        User user3 = User.builder()
                .firstName("Nikita")
                .lastName("Grakov")
                .email("master-daster@mail.ru")
                .build();
        bookService.save(book);
        bookService.save(book2);
        bookService.save(book3);
        userService.save(user);
        userService.save(user2);
        userService.save(user3);

    }
}
