package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Account;
import com.example.entity.Message;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() > 255) {
            return null;
        }

        return messageRepository.save(message);
    }

    public Message deleteMessageById(Integer id){
        Message message = messageRepository.findById(id).orElse(null);
    if (message == null) {
        return null;
    }
    messageRepository.deleteById(id);
    return message;
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).orElse(null);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    public List<Message> getAllMessagesByUser(int id) {
        return messageRepository.findByPostedBy(id);
    }

    public Message getMessageById(Integer id) {
        Message message = messageRepository.findById(id).orElse(null);
        return message;
    }

    public Integer updateMessage(Integer messageId, String newText) {
    Message message = messageRepository.findById(messageId).orElse(null);
    if (message == null || newText == null || newText.isBlank() || newText.length() > 255) {
        return 0;
    }
    message.setMessageText(newText);
    messageRepository.save(message);
    return 1;
}
}
