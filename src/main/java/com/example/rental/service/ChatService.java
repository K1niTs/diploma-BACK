package com.example.rental.service;

import com.example.rental.dto.ChatRoomDto;
import com.example.rental.dto.MessageDto;
import com.example.rental.entity.Message;
import com.example.rental.repo.MessageRepo;
import com.example.rental.repo.UserRepo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    private final MessageRepo repo;
    private final UserRepo    users;

    public ChatService(MessageRepo repo, UserRepo users) {
        this.repo  = repo;
        this.users = users;
    }

    public MessageDto send(MessageDto dto, Long me) {
        if (!me.equals(dto.fromId()))
            throw new AccessDeniedException("Not yourself");

        Message m = new Message();
        m.setSender   (users.findById(dto.fromId()).orElseThrow());
        m.setRecipient(users.findById(dto.toId()  ).orElseThrow());
        m.setText     (dto.text());
        m = repo.save(m);

        return new MessageDto(
                m.getId(),
                m.getSender().getId(),
                m.getRecipient().getId(),
                m.getText(),
                m.getCreatedAt(),
                m.getSender().getEmail()
        );
    }

    public List<MessageDto> history(Long me, Long other) {
        return repo.findConversation(me, other).stream()
                .map(m -> new MessageDto(
                        m.getId(),
                        m.getSender().getId(),
                        m.getRecipient().getId(),
                        m.getText(),
                        m.getCreatedAt(),
                        m.getSender().getEmail()
                ))
                .toList();
    }

    public List<ChatRoomDto> listChats(Long me) {
        List<Message> all = repo.findAllForUser(me);
        Map<Long, ChatRoomDto> rooms = new LinkedHashMap<>();
        for (Message m : all) {
            boolean iSent = m.getSender().getId().equals(me);
            Long  otherId    = iSent
                    ? m.getRecipient().getId()
                    : m.getSender().getId();
            String otherEmail = iSent
                    ? m.getRecipient().getEmail()
                    : m.getSender().getEmail();
            if (!rooms.containsKey(otherId)) {
                rooms.put(otherId, new ChatRoomDto(
                        otherId,
                        otherEmail,
                        m.getText(),
                        m.getCreatedAt()
                ));
            }
        }
        return new ArrayList<>(rooms.values());
    }
}