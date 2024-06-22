package com.appjam.come_with_me.security.filter;

import com.appjam.come_with_me.security.auth.PrincipalDetails;
import com.appjam.come_with_me.service.UserService;
import com.appjam.come_with_me.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {


    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractIdToken(request);
        if (token != null) {
            User user = userService.getUserByUserToken(token);
            if (user != null) {
                saveAuthentication(user);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractIdToken(HttpServletRequest request) {
        if (request.getHeader("token") == null || request.getHeader("token").isBlank()) {
            return null;
        }
        String token = request.getHeader("token");
        if (token != null) {
            return token;
        }
        return null;
    }

    public void saveAuthentication(User myUser) {

        PrincipalDetails userDetailsUser = new PrincipalDetails(myUser);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, "", userDetailsUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
