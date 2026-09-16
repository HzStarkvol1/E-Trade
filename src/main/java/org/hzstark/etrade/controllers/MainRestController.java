package org.hzstark.etrade.controllers;

import org.hzstark.etrade.data.UserEntity;
import org.hzstark.etrade.data.UserRepository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {
    private final UserRepository repository;
    public MainRestController(UserRepository repository)
    {
        this.repository = repository;
    }

}
