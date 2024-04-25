package com.apirest.springsecuritydemo4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apirest.springsecuritydemo4.entity.OurUser;

@Repository
public interface OurUserRepository extends JpaRepository<OurUser, Integer>{
    
    @Query(value = "SELECT * FROM ourusers WHERE email = ?1", nativeQuery = true)
    Optional<OurUser> findByEmail(String email);
}
