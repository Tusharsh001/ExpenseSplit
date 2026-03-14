package com.tushar.split.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping
    public String working(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username= auth.getName();

        return "welcome Mr. "+username;
    }
}
