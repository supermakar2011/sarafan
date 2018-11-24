package com.example.sarafan.repo;

import com.example.sarafan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<User,String> {
}
