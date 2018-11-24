package com.example.sarafan.controller;

import com.example.sarafan.model.Message;
import com.example.sarafan.model.Views;
import com.example.sarafan.repo.MessageRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> show(){
        List<Message> messages = messageRepo.findAll();
        return messages;
    }
    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message oneMessage(@PathVariable("id")Message message){
        return message;
    }
    @PostMapping
    public Message addMessage(@RequestBody Message message){
        message.setCreationDate(LocalDateTime.now());
        return messageRepo.save(message);
    }
    @PutMapping("{id}")
    public Message putMessage(@PathVariable("id") Message messageForDb,@RequestBody Message message){
        BeanUtils.copyProperties(message,messageForDb,"id");
        messageForDb.setCreationDate(LocalDateTime.now());
        return messageRepo.save(messageForDb);
    }
    @DeleteMapping("{id}")
    public void deleteMessage(@PathVariable("id") Message message){
        messageRepo.delete(message);

    }

}
