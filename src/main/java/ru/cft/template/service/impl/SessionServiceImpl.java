package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.BannedToken;
import ru.cft.template.entity.Session;
import ru.cft.template.entity.User;
import ru.cft.template.exception.AccessRightsException;
import ru.cft.template.exception.SessionNotFoundException;
import ru.cft.template.jwt.JwtTokenUtils;
import ru.cft.template.mapper.SessionMapper;
import ru.cft.template.model.request.LoginByEmailBody;
import ru.cft.template.model.request.LoginByPhoneBody;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;
import ru.cft.template.repository.BannedTokenRepository;
import ru.cft.template.repository.SessionRepository;
import ru.cft.template.repository.UserRepository;
import ru.cft.template.service.SessionService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserServiceImpl userService;
    private final SessionRepository sessionRepository;
    private final BannedTokenRepository bannedTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SessionResponse createSessionByEmail(LoginByEmailBody body) {
        User user = userRepository.findByEmail(body.email())
                .filter(u -> passwordEncoder.matches(body.password(), u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect email or password"));

        String token = jwtTokenUtils.generateToken(user);
        Date expiredDate = jwtTokenUtils.getExpirationDateFromToken(token);

        Session session = new Session();
        session.setUserId(user.getId());
        session.setToken(token);
        session.setExpirationTime(expiredDate);
        sessionRepository.save(session);

        return SessionMapper.mapSessionResponse(session);
    }

    @Override
    public SessionResponse loginByPhone(LoginByPhoneBody body) {
        User user = userRepository.findByPhone(body.phone())
                .filter(u -> passwordEncoder.matches(body.password(), u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect phone or password"));

        String token = jwtTokenUtils.generateToken(user);
        Date expiredDate = jwtTokenUtils.getExpirationDateFromToken(token);

        Session session = new Session();
        session.setUserId(user.getId());
        session.setToken(token);
        session.setExpirationTime(expiredDate);
        sessionRepository.save(session);

        return SessionMapper.mapSessionResponse(session);
    }

    @Override
    public List<CurrentSessionResponse> getAllSessions(Authentication authentication) {
        User user = userService.getUserByAuthentication(authentication);

        List<Session> sessions = sessionRepository.findByUserId(user.getId());
        Date now = new Date();
        return sessions.stream()
                .map(session -> {
                    boolean isActive = session.getExpirationTime().after(now);
                    return SessionMapper.mapCurrentSessionResponse(session, isActive);
                })
                .toList();
    }

    @Override
    public List<CurrentSessionResponse> getAllSessionsByActive(Authentication authentication, Boolean active) {
        User user = userService.getUserByAuthentication(authentication);

        List<Session> sessions = sessionRepository.findByActive(active);
        return sessions.stream()
                .map(session -> {
                    return SessionMapper.mapCurrentSessionResponse(session, active);
                })
                .toList();
    }

    @Override
    public CurrentSessionResponse getCurrentSession(Authentication authentication) {
        String currentToken = (String) authentication.getCredentials();

        Optional<Session> sessionOptional = sessionRepository.findByToken(currentToken);
        if (sessionOptional.isPresent()) {
            Session session = sessionOptional.get();
            Date now = new Date();
            return SessionMapper.mapCurrentSessionResponse(
                    session,
                    jwtTokenUtils.getExpirationDateFromToken(currentToken).after(now)
            );
        } else {
            throw new SessionNotFoundException("Session not found");
        }
    }

    @Override
    public ResponseEntity<?> deleteSessionById(Authentication authentication, String id) {
        UUID currentUserId = userService.getUserByAuthentication(authentication).getId();
        UUID sessionId = UUID.fromString(id);

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        if (!session.getUserId().equals(currentUserId)) {
            throw new AccessRightsException("You can only delete your own sessions");
        }

        BannedToken bannedToken = new BannedToken();
        bannedToken.setToken(authentication.getCredentials().toString());

        bannedTokenRepository.save(bannedToken);
        sessionRepository.deleteById(sessionId);

        return ResponseEntity.ok().build();
    }

    @Override
    public SessionResponse logout(Authentication authentication) {
        String currentToken = (String) authentication.getCredentials();
        log.info("Взят токен");

        Session session = sessionRepository.findByToken(currentToken).orElse(null);

        log.info("Найдена сессия");

//        if (!session.getUserId().equals(userId)) {
//            throw new AccessRightsException("You can only logout from your own sessions");
//        }

        BannedToken bannedToken = new BannedToken();
        bannedToken.setToken(authentication.getCredentials().toString());
        bannedTokenRepository.save(bannedToken);

        log.info("Токен забанен");

        session.setActive(false);
        sessionRepository.save(session);

        log.info("Сохранено");

        return SessionMapper.mapSessionResponse(session);
    }
}
