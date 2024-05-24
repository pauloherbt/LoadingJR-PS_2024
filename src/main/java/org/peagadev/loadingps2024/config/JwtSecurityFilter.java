package org.peagadev.loadingps2024.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.repository.UserRepository;
import org.peagadev.loadingps2024.domain.services.JwtService;
import org.peagadev.loadingps2024.domain.services.UserService;
import org.peagadev.loadingps2024.exceptions.ResourceExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("Authorization header is missing");
            filterChain.doFilter(request,response);
            return;
        }
        try{
            String token = authHeader.substring(7);
            UserDetails user = userService.loadUserByUsername((jwtService.getUsername(token)));
            if(jwtService.validToken(token,user)){
                var authToken = UsernamePasswordAuthenticationToken.authenticated(user.getUsername(),null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Successfully authenticated user");
            }
            filterChain.doFilter(request, response);
        }
        catch (BadCredentialsException | AccessDeniedException | JwtException e){
            e.printStackTrace();
            resolver.resolveException(request,response,null,e);
        }
    }
}
