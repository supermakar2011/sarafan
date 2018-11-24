package com.example.sarafan.controller;

import com.example.sarafan.model.User;
import com.example.sarafan.repo.MessageRepo;
import com.example.sarafan.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageRepo messageRepo;
    private final UserDetailsRepo userDetailsRepo;



    @Autowired
    public MainController(MessageRepo messageRepo,UserDetailsRepo userDetailsRepo) {
        this.messageRepo = messageRepo;
        this.userDetailsRepo = userDetailsRepo;
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user){


        HashMap<Object,Object> data  = new HashMap<>();

        data.put("profile",user);
        data.put("messages",messageRepo.findAll());
         model.addAttribute("frontendData", data);
        return "index";
    }
}
