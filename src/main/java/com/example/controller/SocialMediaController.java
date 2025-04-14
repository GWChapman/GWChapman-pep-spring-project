package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.entity.Message;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
@CrossOrigin("*")
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Account account) {
        if (accountService.doesUsernameExist(account.getUsername())) {
            return ResponseEntity.status(409).body("Username Taken");
        }
        if (account.getUsername() == null || account.getUsername().isBlank() ||
                account.getPassword() == null || account.getPassword().isBlank()
                || account.getPassword().length() < 4) {
            return ResponseEntity.badRequest().body("Invalid Username or paswword.");
        }

        return ResponseEntity.ok(accountService.registerAccount(account));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody Account account) {
        Account existingAccount = accountService.loginAccount(account.getUsername(), account.getPassword());

        if (existingAccount != null) {
            return ResponseEntity.ok(existingAccount);
        } 
        else {
            return ResponseEntity.status(401).body("Invalid Username or password.");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if(messageService.getAccountById(message.getPostedBy())==null){
            return ResponseEntity.status(400).body("Invalid User.");
        }
        if (messageService.createMessage(message) != null) {
            return ResponseEntity.ok(messageService.createMessage(message));
        } 
        else {
            return ResponseEntity.status(400).body("Invalid message.");
        }
    }
   @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("message_id") Integer messageId) {
        Message deletedMessage = messageService.deleteMessageById(messageId);
        if (deletedMessage != null) {
            return ResponseEntity.ok(1);
        } 
        else {
            return ResponseEntity.ok().build();
        }

    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/accounts/{user_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable("user_id") Integer userId) {
        return ResponseEntity.ok(messageService.getAllMessagesByUser(userId));
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<?> getMessageById(@PathVariable("message_id") Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            return ResponseEntity.ok(message);
        } 
        else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessage(@PathVariable("message_id") Integer messageId, @RequestBody Message message) {
        int update = messageService.updateMessage(messageId, message.getMessageText());
        if (update ==1) {
            return ResponseEntity.ok(update);
        } 
        else {
            return ResponseEntity.badRequest().body("Invalid message or ID.");
        }
    }
}