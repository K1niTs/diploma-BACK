package com.example.rental.controller;

import com.example.rental.dto.ReviewDto;
import com.example.rental.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instruments/{instrId}/reviews")
public class ReviewController {

    private final ReviewService svc;
    public ReviewController(ReviewService s){ svc = s; }

    @GetMapping
    public List<ReviewDto> list(@PathVariable Long instrId){
        return svc.listForInstrument(instrId);
    }

    @PostMapping
    public ReviewDto add(@PathVariable Long instrId,
                         @RequestBody ReviewDto body,
                         HttpServletRequest req){

        Long uid = (Long) req.getAttribute("uid");
        return svc.add(instrId, uid, body.rating(), body.comment());
    }
}
