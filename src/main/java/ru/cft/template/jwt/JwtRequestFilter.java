package ru.cft.template.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.cft.template.entity.User;
import ru.cft.template.repository.BannedTokenRepository;
import ru.cft.template.repository.UserRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final BannedTokenRepository bannedTokenRepository;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            boolean isBanned = bannedTokenRepository.findByToken(jwt).isPresent();

            if (isBanned) {
                log.info("Attempt to use a banned token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is banned");
                return;
            }
            try {
                UUID userId = jwtTokenUtils.getUserIdFromToken(jwt);

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userRepository.findById(userId).orElse(null);

                    if (user != null) {
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                user, jwt, Collections.emptyList()
                        );
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
            } catch (ExpiredJwtException e) {
                log.info("Token is expired");
            } catch (Exception e) {
                log.info("Error processing JWT", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
