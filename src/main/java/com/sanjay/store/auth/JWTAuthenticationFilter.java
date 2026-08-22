package com.sanjay.store.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException
    {
        var authHeader=request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request,response);
            return;
        }
        var token=authHeader.replace("Bearer ","");

        var jwt=jwtService.parse(token);
        if(jwt==null || jwt.isExpired())
        {
            filterChain.doFilter(request,response);
            return;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+jwt.getRole()));



        var authentication=
                new UsernamePasswordAuthenticationToken(jwt.getUserId(),null
        , authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);


    }
}
