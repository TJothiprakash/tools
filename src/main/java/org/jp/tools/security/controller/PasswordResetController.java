package org.jp.tools.security.controller;

import org.jp.tools.redis.resetpassword.ResetPasswordService;
import org.jp.tools.security.domain.ForgotPasswordRequest;
import org.jp.tools.security.domain.User;
import org.jp.tools.security.repository.UserRepository;
import org.jp.tools.security.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ResetPasswordService resetPasswordService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordResetController(UserRepository userRepository,
                                   EmailService emailService,
                                   ResetPasswordService resetPasswordService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        System.out.println("inside forgotPassword api");

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User with email not found");
        }
        System.out.println("user found " + optionalUser.get().toString());
        User user = optionalUser.get();
        String resetToken = UUID.randomUUID().toString();
        System.out.println("resetToken = " + resetToken);
        System.out.println(" saving the token in redis ... ");
        // ✅ Save token in Redis
        resetPasswordService.storeResetToken(resetToken, user.getEmail());

        // Send email with token
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + resetToken;
        System.out.println("Generated reset link " + resetLink);
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return ResponseEntity.ok("Reset link sent to email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("token") String token,
            @RequestBody Map<String, String> request
    ) {
        String newPassword = request.get("newPassword");
        System.out.println("inside resetPassword api");

        // ✅ Fetch email by token from Redis
        String email = resetPasswordService.getEmailByToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        User user = optionalUser.get();

        // ✅ Set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // ✅ Invalidate token (optional, since Redis TTL will expire)
        resetPasswordService.invalidateToken(token);

        return ResponseEntity.ok("Password reset successful");
    }
}
