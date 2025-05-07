package com.example.rental.controller;

import com.example.rental.dto.ChatRoomDto;
import com.example.rental.dto.MessageDto;
import com.example.rental.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService svc;

    public ChatController(ChatService svc) {
        this.svc = svc;
    }

    @GetMapping("/rooms")
    public List<ChatRoomDto> rooms(HttpServletRequest req) {
        Long me = (Long) req.getAttribute("uid");
        return svc.listChats(me);
    }

    @GetMapping("/{otherId}")
    public List<MessageDto> history(
            @PathVariable Long otherId,
            HttpServletRequest req
    ) {
        Long me = (Long) req.getAttribute("uid");
        return svc.history(me, otherId);
    }

    @PostMapping("/send")
    public MessageDto send(
            @RequestBody MessageDto dto,
            HttpServletRequest req
    ) {
        Long me = (Long) req.getAttribute("uid");
        return svc.send(dto, me);
    }
}
