package com.example.crudapp.controllers;

import com.example.crudapp.models.Person;
import com.example.crudapp.services.PeopleService;
import com.example.crudapp.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", peopleService.getBooksByUserId(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String  newUser(@ModelAttribute("person") Person user) {
        return "people/new";
    }

    @PostMapping()
    public String create (@ModelAttribute("person") @Valid Person user,
                          BindingResult bindingResult) {
        personValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(user);

        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("perosn", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("person") @Valid Person user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, user);

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
