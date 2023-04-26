package com.nikita.library.service;

import com.nikita.library.model.Book;
import com.nikita.library.model.User;
import com.nikita.library.repository.BookRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class BookService {
    private final Integer daysToReturn = 7;
    private final BookRepository bookRepository;
    private final UserService userService;

    private final Logger logger = Logger.getLogger("logger");

    public BookService(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book take(Integer id, Integer userId) {
        Optional<Book> byId = bookRepository.findById(id);
        Optional<User> userById = userService.findUserById(userId);
        if (byId.isEmpty()|userById.isEmpty()) {
            System.out.println("Нет книги или пользовател");
        }
        Book book = byId.get();
        User user = userById.get();
        book.setIsAvailable(false);
        book.setUser(user);
        book.setTimestamp(new Timestamp(new Date().getTime()));
        bookRepository.save(book);
        return book;
    }



    @Scheduled(fixedRate = 600000L)
    public void checkDateToReturn() {
        List<Book> books = bookRepository.findAll();
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
            Book next = iterator.next();
            Optional<Timestamp> timestamp = Optional.ofNullable(next.getTimestamp());
            if (timestamp.isEmpty()) {
                continue;
            }
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime taken = timestamp.get().toLocalDateTime();
            Period period = Period.between(taken.toLocalDate(), now.toLocalDate());
            if (period.getDays() > daysToReturn) {
                sendEmail();
            }
        }
        logger.log(Level.INFO, "check date started");
    }

    private void sendEmail() {
    }

    public Book back(Integer id) {
        Optional<Book> byId = bookRepository.findById(id);
        if (byId.isEmpty()) {
            System.out.println("Нет книги");
        }
        Book book = byId.get();
        book.setUser(null);
        book.setIsAvailable(true);
        book.setTimestamp(null);
        bookRepository.save(book);
        return book;
    }
}
