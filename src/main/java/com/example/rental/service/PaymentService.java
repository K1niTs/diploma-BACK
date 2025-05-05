package com.example.rental.service;

import com.example.rental.dto.PaymentDto;
import com.example.rental.entity.*;
import com.example.rental.repo.*;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepo payments;
    private final BookingRepo bookings;
    public PaymentService(PaymentRepo p,BookingRepo b){payments=p;bookings=b;}

    public PaymentDto pay(Long bookingId){
        Booking b=bookings.findById(bookingId).orElseThrow();
        if(!"NEW".equals(b.getStatus())) throw new RuntimeException("Already paid/cancelled");
        b.setStatus("PAID");
        bookings.save(b);

        Payment p=new Payment();
        p.setBooking(b);
        p.setAmount(b.getTotalCost());
        p.setPaymentMethod("FAKE");
        p.setPaymentStatus("SUCCESS");
        payments.save(p);

        return new PaymentDto(p.getId(), b.getId(), p.getAmount(),
                p.getPaymentMethod(), p.getPaymentStatus());
    }
}
