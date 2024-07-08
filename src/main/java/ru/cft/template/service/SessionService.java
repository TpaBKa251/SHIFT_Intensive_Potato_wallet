package ru.cft.template.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.cft.template.entity.Session;
import ru.cft.template.model.request.LoginByEmailBody;
import ru.cft.template.model.request.LoginByPhoneBody;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;

import java.util.List;
import java.util.UUID;

public interface SessionService {
    SessionResponse createSessionByEmail(LoginByEmailBody body);

    SessionResponse loginByPhone(LoginByPhoneBody body);

    List<CurrentSessionResponse> getAllSessions(Authentication authentication);

    List<CurrentSessionResponse> getAllSessionsByActive(Authentication authentication, Boolean active);

    CurrentSessionResponse getCurrentSession(Authentication authentication);

    ResponseEntity<?> deleteSessionById(Authentication authentication, String id);
}
