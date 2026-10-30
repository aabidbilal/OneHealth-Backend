package in.onehealth.demo.auth.controller;

import in.onehealth.demo.auth.dto.LoginRequest;
import in.onehealth.demo.auth.dto.LoginResponse;
import in.onehealth.demo.auth.dto.RegisterRequest;
import in.onehealth.demo.auth.dto.RegisterResponse;
import in.onehealth.demo.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid
            @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt
            ) {
        return Map.of(
                "userId", jwt.getSubject(),
                "role", jwt.getClaimAsString("role")
        );
    }
}