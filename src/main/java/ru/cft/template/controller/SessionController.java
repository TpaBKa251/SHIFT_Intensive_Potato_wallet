package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.request.LoginByEmailBody;
import ru.cft.template.model.request.LoginByPhoneBody;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;
import ru.cft.template.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("potato/api/users")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/loginviaemail")
    public SessionResponse createSession(@RequestBody LoginByEmailBody body) {
        return sessionService.createSessionByEmail(body);
    }

    @PostMapping("/loginviaphone")
    public SessionResponse createSession(@RequestBody LoginByPhoneBody body) {
        return sessionService.loginByPhone(body);
    }


    @GetMapping("/sessions")
    public List<CurrentSessionResponse> getAllSessions(Authentication authentication) {
        return sessionService.getAllSessions(authentication);
    }

    @GetMapping("/sessions/current")
    public CurrentSessionResponse getCurrentSession(Authentication authentication) {
        return sessionService.getCurrentSession(authentication);
    }

    @GetMapping("/sessions/byactive/{active}")
    public ResponseEntity<List<CurrentSessionResponse>> getCurrentSession(Authentication authentication, @PathVariable Boolean active) {
        return ResponseEntity.ok(sessionService.getAllSessionsByActive(authentication, active));
    }


    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> deleteSession(Authentication authentication, @PathVariable String id) {
        return ResponseEntity.ok(sessionService.deleteSessionById(authentication, id));
    }
}
