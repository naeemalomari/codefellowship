package com.codefellowship.restap.controller;


import com.codefellowship.restap.Repository.ApplicationUserRepository;
import com.codefellowship.restap.model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;

@Controller
public class HomeController {


    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String getHomePage() {
        return "homepage";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }
    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userDetails", userDetails);
        return "profile";
    }


    @PostMapping("/signup")
    public RedirectView attemptSignUp(@RequestParam String username, @RequestParam String password) {
        ApplicationUser applicationUser = new ApplicationUser(username, passwordEncoder.encode(password));
        applicationUser = applicationUserRepository.save(applicationUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(applicationUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/");
    }


}
