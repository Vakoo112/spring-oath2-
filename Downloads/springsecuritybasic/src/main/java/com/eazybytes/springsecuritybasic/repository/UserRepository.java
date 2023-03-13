/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.eazybytes.springsecuritybasic.repository;

import com.eazybytes.springsecuritybasic.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vako
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByEmail(String email);

     Optional<User> findByUsername(String username);
}
