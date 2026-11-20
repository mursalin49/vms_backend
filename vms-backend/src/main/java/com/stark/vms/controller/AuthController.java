package com.stark.vms.controller;
import com.stark.vms.entity.AppUser;
import com.stark.vms.payload.LoginRequest;
import com.stark.vms.payload.LoginResponse;
import com.stark.vms.repository.AppUserRepository;
import com.stark.vms.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** Login only. There is no public sign-up: the authorized user is seeded at startup (see DataSeeder). */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    public AuthController(AppUserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest r) {
        AppUser u = users.findByEmail(r.email().trim().toLowerCase())
                .filter(x -> encoder.matches(r.password(), x.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        return new LoginResponse(jwt.generate(u.getEmail()), u.getEmail());
    }
}
