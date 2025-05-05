package com.example.rental.controller;

import com.example.rental.dto.AuthRequest;
import com.example.rental.dto.AuthResponse;
import com.example.rental.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService s){service=s;}

    @PostMapping("/register") public AuthResponse reg(@RequestBody AuthRequest req){ return service.register(req);}
    @PostMapping("/login")    public AuthResponse log(@RequestBody AuthRequest req){ return service.login(req);}
}
