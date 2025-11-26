package com.arqui.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.arqui.crud.entity.User;
import com.arqui.crud.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String listUsers(Model model) {

        model.addAttribute("users", userRepository.findAll());

        model.addAttribute("newUser", new User());

        return "index";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("newUser") User user) {
        userRepository.save(user);
        return "redirect:/";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {        
        userRepository.deleteById(id);
        return "redirect:/";
    }

}
