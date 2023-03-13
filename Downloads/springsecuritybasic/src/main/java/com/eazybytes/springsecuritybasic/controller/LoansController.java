/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eazybytes.springsecuritybasic.controller;

import com.eazybytes.springsecuritybasic.model.Loans;
import com.eazybytes.springsecuritybasic.model.User;
import com.eazybytes.springsecuritybasic.repository.LoanRepository;
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
public class LoansController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/myLoans")
    public List<Loans> getLoanDetails(@RequestParam String email) {
        List<User> users = userRepository.findByEmail(email);
        if (users != null && !users.isEmpty()) {
            List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(users.get(0).getId());
            if (loans != null ) {
                return loans;
            }
        }
        return null;
    }

}