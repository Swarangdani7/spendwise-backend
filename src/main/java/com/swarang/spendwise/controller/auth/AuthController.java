package com.swarang.spendwise.controller.auth;

import com.swarang.spendwise.dto.auth.AuthResponseDTO;
import com.swarang.spendwise.dto.auth.LoginRequestDTO;
import com.swarang.spendwise.dto.auth.SignupRequestDTO;
import com.swarang.spendwise.dto.auth.SignupResponseDTO;
import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.service.auth.LoginService;
import com.swarang.spendwise.service.auth.LogoutService;
import com.swarang.spendwise.service.auth.SignupService;
import com.swarang.spendwise.service.auth.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "Authentication APIs")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        AuthUser authUser = signupService.signupAndCreateProfile(signupRequestDTO);
        SignupResponseDTO response = new SignupResponseDTO("User registered successfully", authUser.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse) {
        AuthUser authUser = loginService.validateCredentials(loginRequestDTO);
        String accessToken = tokenService.generateAccessToken(authUser.getEmail());
        String refreshToken = tokenService.generateRefreshToken(authUser);

        AuthResponseDTO response = new AuthResponseDTO(authUser.getId(), accessToken, "Bearer");
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set true for HTTPS
        cookie.setPath("/auth");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refresh_token") String refreshCookie, HttpServletResponse httpServletResponse) {
        ProfileUser profileUser = tokenService.validateRefreshToken(refreshCookie);
        logoutService.invalidateUserSession(profileUser); // clearing refresh token

        Cookie emptyRefreshCookie = new Cookie("refresh_token", "");
        emptyRefreshCookie.setMaxAge(0);
        emptyRefreshCookie.setHttpOnly(true);
        httpServletResponse.addCookie(emptyRefreshCookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refresh_token") String refreshCookie) {
        ProfileUser profileUser = tokenService.validateRefreshToken(refreshCookie);
        AuthUser authUser = tokenService.getAuthCredentials(profileUser);

        String newAccessToken = tokenService.generateAccessToken(authUser.getEmail());
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(authUser.getId(), newAccessToken, "Bearer");
        return ResponseEntity.ok(authResponseDTO);
    }
}
