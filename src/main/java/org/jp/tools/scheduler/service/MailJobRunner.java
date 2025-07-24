package org.jp.tools.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailJobRunner {

    private final JavaMailSender mailSender;

    public void run(String payload) {
        try {
            System.out.println("📨 MailJobRunner received payload: " + payload);

            String[] parts = payload.split("\\|");
            if (parts.length < 3) {
                System.err.println("❌ Invalid payload format. Expected: to|subject|body");
                return;
            }

            String to = parts[0];
            String subject = parts[1];
            String body = parts[2];

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("✅ Mail sent to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Failed to send mail: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
