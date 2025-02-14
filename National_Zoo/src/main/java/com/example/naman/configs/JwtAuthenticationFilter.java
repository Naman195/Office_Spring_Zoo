package com.example.naman.configs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.naman.entities.Token;
import com.example.naman.services.JwtService;
import com.example.naman.services.TokenCache;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
   
    private final TokenCache tokenCache;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        HandlerExceptionResolver handlerExceptionResolver,
        TokenCache tokenCache
    ) {
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.tokenCache = tokenCache;
    }

    /**
     * OverRide the doFilterInternal Method
     * 
     */
    
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {

                if (jwtService.isTokenValid(jwt)) {
                	 @SuppressWarnings("unchecked")
                	 List<String> authori = (List<String>)jwtService.extractAllClaims(jwt).get("authorities");
                	 
                	 List<GrantedAuthority> authorities = authori.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                	 
                	 Token token = tokenCache.findTokenByToken(jwt);
                	 if(token != null && token.getExpiresAt().isAfter(LocalDateTime.now())) {
                		 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                 userEmail,
                                 null,
                                 authorities
                         );
                		 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                         SecurityContextHolder.getContext().setAuthentication(authToken);
                	 } else {
                		 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                         return;
                	 }
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}