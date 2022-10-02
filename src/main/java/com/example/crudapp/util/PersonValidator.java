package com.example.crudapp.util;

import com.example.crudapp.models.Person;
import com.example.crudapp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService userService) {
        this.peopleService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person user = (Person) target;

        if (peopleService.getUserByName(user.getName()).isPresent()) {
            errors.rejectValue("name", "", "A person with this name already exists");
        }

    }
}
