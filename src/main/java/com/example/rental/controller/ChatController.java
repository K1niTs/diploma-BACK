// src/main/java/com/example/rental/controller/ChatController.java
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

    /** GET /chat/rooms — список всех комнат (диалогов) */
    @GetMapping("/rooms")
    public List<ChatRoomDto> rooms(HttpServletRequest req) {
        Long me = (Long) req.getAttribute("uid");
        return svc.listChats(me);
    }

    /** GET /chat/{otherId} — история конкретного диалога */
    @GetMapping("/{otherId}")
    public List<MessageDto> history(
            @PathVariable Long otherId,
            HttpServletRequest req
    ) {
        Long me = (Long) req.getAttribute("uid");
        return svc.history(me, otherId);
    }

    /** POST /chat/send — отправить сообщение */
    @PostMapping("/send")
    public MessageDto send(
            @RequestBody MessageDto dto,
            HttpServletRequest req
    ) {
        Long me = (Long) req.getAttribute("uid");
        return svc.send(dto, me);
    }
}
