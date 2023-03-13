/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eazybytes.springsecuritybasic.controller;

import com.eazybytes.springsecuritybasic.model.Cards;
import com.eazybytes.springsecuritybasic.model.User;
import com.eazybytes.springsecuritybasic.repository.CardsRepository;
import com.eazybytes.springsecuritybasic.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vako
 */
@RestController
public class CardsController {

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/myCards")
    public List<Cards> getCardDetails(@RequestParam String email) {
        List<User> users = userRepository.findByEmail(email);
        if (users != null && !users.isEmpty()) {
            List<Cards> cards = cardsRepository.findByCustomerId(users.get(0).getId());
            if (cards != null ) {
                return cards;
            }
        }
        return null;
    }

}