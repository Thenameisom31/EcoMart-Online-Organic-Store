package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDashRepository extends JpaRepository<User, Long> {
    
    // custom query for user by email
    User findByEmail(String email);
}
