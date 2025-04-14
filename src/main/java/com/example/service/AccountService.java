package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() || 
            account.getPassword() == null ||account.getPassword().isBlank()|| account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid Username or paswword.");
            }
        if (doesUsernameExist(account.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
    
        return accountRepository.save(account);
    }

    public boolean doesUsernameExist(String username) {
        return accountRepository.existsByUsername(username);
    }

    public Account loginAccount(String username, String password){
        Account account = accountRepository.findByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }
}



