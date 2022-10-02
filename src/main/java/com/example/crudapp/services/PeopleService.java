package com.example.crudapp.services;

import com.example.crudapp.models.Book;
import com.example.crudapp.models.Person;
import com.example.crudapp.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository usersRepository;

    @Autowired
    public PeopleService(PeopleRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Person> findAll() {
        return usersRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> user = usersRepository.findById(id);
        return user.orElse(null);
    }

    @Transactional
    public void save(Person user){
        usersRepository.save(user);
    }

    @Transactional
    public void update(int id, Person user) {
        user.setId(id);
        usersRepository.save(user);
    }

    @Transactional
    public void delete(int id) {
        usersRepository.deleteById(id);
    }

    public Optional<Person> getUserByName(String name) {

        return usersRepository.findByName(name);
    }

    public List<Book> getBooksByUserId(int id) {
        Optional<Person> user = usersRepository.findById(id);

        if (user.isPresent()){
            Hibernate.initialize(user.get().getBooks());

            user.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                if (diffInMillies > 864000000)
                    book.setExpired(true);
            });

            return user.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
