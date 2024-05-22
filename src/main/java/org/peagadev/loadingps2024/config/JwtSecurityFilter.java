package org.peagadev.loadingps2024.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.repository.UserRepository;
import org.peagadev.loadingps2024.domain.services.JwtService;
import org.peagadev.loadingps2024.domain.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

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
            UserDetails user = userRepository.findByEmail(jwtService.getUsername(token)).orElseThrow();
            if(jwtService.validToken(token,user)){
                var authToken = UsernamePasswordAuthenticationToken.authenticated(user.getUsername(),null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Successfully authenticated user");
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
