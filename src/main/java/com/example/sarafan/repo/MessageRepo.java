package com.example.sarafan.repo;

import com.example.sarafan.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message,Long> {

}
