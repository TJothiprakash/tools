package org.jp.tools.scheduler.service;

import org.jp.tools.scheduler.model.ScheduledJob;
import org.springframework.stereotype.Service;

@Service
public class JobExecutor {


    private final MailJobRunner mailJobRunner;

    public JobExecutor(MailJobRunner mailJobRunner) {
        this.mailJobRunner = mailJobRunner;
    }

    public void execute(ScheduledJob job) {
        switch (job.getJobType().toUpperCase()) {
            case "MAIL":
                System.out.println("📧 Sending mail for job ID: " + job.getId());
                // TODO: add actual email logic
                System.out.println("📧 Sending mail for job ID: " + job.getId());
//                mailJobRunner.run(job.getPayload()); // ✅ send the email

                break;
            case "PING":
                System.out.println("📡 Pinging for job ID: " + job.getId());
                break;
            case "CHAT":
                System.out.println("💬 Chat job for ID: " + job.getId());
                break;
            default:
                System.out.println("❓ Unknown job type for ID: " + job.getId());
        }
    }
}
