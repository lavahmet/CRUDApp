package com.example.crudapp.services;

import com.example.crudapp.models.Book;
import com.example.crudapp.models.Person;
import com.example.crudapp.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findById(int id) {
        Optional<Book> book = booksRepository.findById(id);

        return book.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        Book bookToBeUpdated = booksRepository.findById(id).get();

        book.setId(id);
        book.setOwner(bookToBeUpdated.getOwner());

        booksRepository.save(bookToBeUpdated);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }


    public Person getBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                }
        );
    }

    @Transactional
    public void assign(int id, Person user) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(user);
                    book.setTakenAt(new Date());
                }
        );
    }

    public List<Book> serchByTitle(String title) {

        return booksRepository.findByTitleStartingWith(title);
    }

}
