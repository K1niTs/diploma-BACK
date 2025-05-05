package com.example.rental.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class FakeAuthFilter implements Filter {
    public static final String HEADER = "X-User-Id";
    @Override public void doFilter(ServletRequest r, ServletResponse s, FilterChain c)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) r;
        if (req.getHeader(HEADER) != null)
            req.setAttribute("uid", Long.parseLong(req.getHeader(HEADER)));
        c.doFilter(r, s);
    }
}
