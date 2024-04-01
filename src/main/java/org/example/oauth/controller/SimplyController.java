package org.example.oauth.controller;


import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SimplyController {

    @GetMapping("/")
    public String getPublicPage(){
        return "Public";
    }

    @GetMapping("/not-public")
    public String getNotPublicPage(){
        return "Not public ";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user")
    public String getOnlyForAdmin(Principal principal){
        return "Hello " + principal.getName();
    }

    @GetMapping("/info")
    public String getInfo(Principal principal){
        return "Hello " + principal.getName() + ", your info:" + principal;
    }
}
