package com.example.rental.controller;

import com.example.rental.dto.PaymentDto;
import com.example.rental.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/payments")
public class PaymentController {
    private final PaymentService svc;
    public PaymentController(PaymentService s){svc=s;}

    @PostMapping("/fake")
    public PaymentDto fakePay(@RequestParam Long bookingId){
        return svc.pay(bookingId);
    }
}
