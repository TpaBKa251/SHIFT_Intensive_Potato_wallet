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
    public ResponseEntity<SessionResponse> createSession(@RequestBody LoginByEmailBody body){
        return ResponseEntity.ok(sessionService.createSessionByEmail(body));
    }

    @PostMapping("/loginviaphone")
    public ResponseEntity<SessionResponse> createSession(@RequestBody LoginByPhoneBody body){
        return ResponseEntity.ok(sessionService.loginByPhone(body));
    }


    @GetMapping("/sessions")
    public ResponseEntity<List<CurrentSessionResponse>> getAllSessions(Authentication authentication){
        return ResponseEntity.ok(sessionService.getAllSessions(authentication));
    }

    @GetMapping("/sessions/current")
    public ResponseEntity<CurrentSessionResponse> getCurrentSession(Authentication authentication){
        return ResponseEntity.ok(sessionService.getCurrentSession(authentication));
    }

    @GetMapping("/sessions/byactive")
    public ResponseEntity<List<CurrentSessionResponse>> getCurrentSession(Authentication authentication, Boolean active){
        return ResponseEntity.ok(sessionService.getAllSessionsByActive(authentication, active));
    }


    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> deleteSession(Authentication authentication, @PathVariable String id){
        return ResponseEntity.ok(sessionService.deleteSessionById(authentication, id));
    }
}
