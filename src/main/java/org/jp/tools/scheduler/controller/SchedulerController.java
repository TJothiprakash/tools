package org.jp.tools.scheduler.controller;

import lombok.RequiredArgsConstructor;
import org.jp.tools.scheduler.model.ScheduledJob;
import org.jp.tools.scheduler.repository.ScheduledJobRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class SchedulerController {

    private final ScheduledJobRepository jobRepository;

    @PostMapping("/mail")
    public ScheduledJob scheduleMailJob(@RequestBody ScheduledJob job) {
        job.setJobType("MAIL");
        job.setActive(true);
        job.setNextExecutionTime(LocalDateTime.now().plusMinutes(1)); // or parse cron
        return jobRepository.save(job);
    }
}

