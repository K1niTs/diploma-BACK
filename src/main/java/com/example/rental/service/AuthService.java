package com.example.rental.service;

import com.example.rental.dto.*;
import com.example.rental.entity.User;
import com.example.rental.exception.EmailBusyException;
import com.example.rental.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepo users;
    public AuthService(UserRepo users){this.users=users;}

    public AuthResponse register(AuthRequest req){
        if (users.existsByEmail(req.email()))
            throw new EmailBusyException();
        User u=new User();
        u.setEmail(req.email());
        u.setName(req.name());
        u.setPasswordHash(BCrypt.hashpw(req.password(),BCrypt.gensalt()));
        users.save(u);
        return new AuthResponse(u.getId(),u.getEmail(),"FAKE_TOKEN_"+u.getId());
    }
    public AuthResponse login(AuthRequest req){
        User u=users.findByEmail(req.email());
        if(u==null||!BCrypt.checkpw(req.password(),u.getPasswordHash()))
            throw new RuntimeException("Bad creds");
        return new AuthResponse(u.getId(),u.getEmail(),"FAKE_TOKEN_"+u.getId());
    }
}
