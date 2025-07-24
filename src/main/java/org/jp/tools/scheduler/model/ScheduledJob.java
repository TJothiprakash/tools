package org.jp.tools.scheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ScheduledJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobType; // e.g., "MAIL", "PING", "CHAT"
    private String cronExpression; // e.g., "0 0 * * * *"
    private String payload; // JSON or plain content (e.g., email body, API URL)
    private LocalDateTime lastRunTime;
    private boolean active;
    private LocalDateTime nextExecutionTime;
}
