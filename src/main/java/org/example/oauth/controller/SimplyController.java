package org.example.oauth.controller;


import org.example.oauth.entity.User;
import org.example.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SimplyController {

    private final UserService userService;

    @Autowired
    public SimplyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getPublicPage(){
        return "Public";
    }

    @GetMapping("/not-public")
    public String getNotPublicPage(){
        return "Not public ";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String getOnlyForUser(){
        return "Hello user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String getOnlyForAdmin(){
        return "Hello admin";
    }

    @GetMapping("/info")
    public String getInfo(Principal principal){
        return "Hello " + principal.getName() + ", your info:" + principal;
    }

    @PostMapping("/registration")
    public void registration(@RequestBody User user) {
        userService.save(user);
    }
}
