package ru.cft.template.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.cft.template.entity.BannedToken;
import ru.cft.template.entity.Session;
import ru.cft.template.entity.User;
import ru.cft.template.repository.BannedTokenRepository;
import ru.cft.template.repository.SessionRepository;
import ru.cft.template.repository.UserRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final BannedTokenRepository bannedTokenRepository;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
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
                if (userId != null) {
                    User user = userRepository.findById(userId).orElse(null);

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            user, jwt, Collections.emptyList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (ExpiredJwtException e) {
                log.debug("Token is expired :(");

                Session session = sessionRepository.findByToken(jwt).orElse(null);
                BannedToken bannedToken = new BannedToken();

                bannedToken.setToken(jwt);
                bannedTokenRepository.save(bannedToken);

                if (session != null) {
                    session.setActive(false);
                    sessionRepository.save(session);
                }

                throw e;
            } catch (Exception e) {
                log.error("Error processing JWT", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}

