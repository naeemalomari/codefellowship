package com.codefellowship.restap.controller;


import com.codefellowship.restap.Repository.ApplicationUserRepository;
import com.codefellowship.restap.Repository.PostRepository;
import com.codefellowship.restap.model.ApplicationUser;
import com.codefellowship.restap.model.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {


    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    PostRepository postRepository;


    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView attemptSignUp(@ModelAttribute ApplicationUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("login")
    public RedirectView loginPage(@RequestParam String username) {
        ApplicationUser user = applicationUserRepository.findApplicationUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/homepage2");
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser user = applicationUserRepository.findApplicationUserByUsername(userDetails.getUsername());
        model.addAttribute("userDetails", user);

        return "profile";
    }

    @GetMapping("/posts")
    public String getPosts(@ModelAttribute Posts posts, Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Posts> post = postRepository.findAllByUserUsername(userDetails.getUsername());
        model.addAttribute("posts", post);

        return "posts";
    }
    @PostMapping("/posts")
    public RedirectView addPosts(@ModelAttribute Posts posts) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser user = applicationUserRepository.findApplicationUserByUsername(userDetails.getUsername());
        System.out.println(userDetails.getUsername());
        posts.setUser(user);
        applicationUserRepository.save(user);
        postRepository.save(posts);
        user.setPosts(Collections.singletonList(posts));

        return new RedirectView("/profile") ;
    }
    @GetMapping("/profile/{id}")
    public String getProfilePageById(@PathVariable String id , Model model) {
        long Id= Long.parseLong(id);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser user = applicationUserRepository.findApplicationUserById(Id);
        model.addAttribute("user", user);

        return "userId";
    }

    @GetMapping("/home")
    public String gethome(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser user=applicationUserRepository.findApplicationUserByUsername(userDetails.getUsername());
        model.addAttribute("userdata", user);
        return "homepage2";
    }

    @GetMapping("/search")
    public String getAllusers(Model model){
        List<ApplicationUser> users = applicationUserRepository.findAll();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser current=applicationUserRepository.findApplicationUserByUsername(userDetails.getUsername());
        model.addAttribute("actual", current.getUsername());
        model.addAttribute("users", users);
        return "search";
    }

    @GetMapping("/feed")
    public String viewFeed(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser user=applicationUserRepository.findApplicationUserByUsername(userDetails.getUsername());
        Set<ApplicationUser> userFeed=user.getFollowers();

        model.addAttribute("following", userFeed);
        return "feed";
    }
    @GetMapping("/")
    public String goHome(){
        return "homepage2";
    }
}