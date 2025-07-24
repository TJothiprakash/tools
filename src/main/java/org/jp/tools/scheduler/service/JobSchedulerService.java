package org.jp.tools.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.jp.tools.scheduler.model.ScheduledJob;
import org.jp.tools.scheduler.repository.ScheduledJobRepository;
import org.jp.tools.scheduler.service.JobExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
public class JobSchedulerService {

    private final ScheduledJobRepository jobRepository;
    private final JobExecutor jobExecutor;

    // FIFO Queue to simulate sequential processing
    private final BlockingQueue<ScheduledJob> jobQueue = new LinkedBlockingQueue<>();

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void pollAndRunJobs() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        System.out.println("=======================================");
        System.out.println("🕒 Polling jobs at: " + now);

        List<ScheduledJob> jobs = jobRepository.findByActiveTrue();
        System.out.println("📦 Active jobs found: " + jobs.size());

        for (ScheduledJob job : jobs) {
            System.out.println("---------------------------------------");
            System.out.println(">> 🛠 Checking job ID: " + job.getId());
            System.out.println("   ├─ Type: " + job.getJobType());
            System.out.println("   ├─ Cron: " + job.getCronExpression());
            System.out.println("   ├─ Last run time: " + job.getLastRunTime());
            System.out.println("   └─ Now: " + now);
            LocalDateTime lastRun = job.getLastRunTime() != null
                    ? job.getLastRunTime().withSecond(0).withNano(0)
                    : LocalDateTime.now().minusMinutes(1).withSecond(0).withNano(0); // fallback


            // Assume cron expression is "*/1 * * * *" => interval = 1 minute
            int intervalMinutes = parseInterval(job.getCronExpression());
            LocalDateTime nextEligibleRun = lastRun.plusMinutes(intervalMinutes);

            if (!nextEligibleRun.isAfter(now)) {
                System.out.println("✅ Queuing job ID: " + job.getId());
                job.setLastRunTime(now); // update before queuing
                jobRepository.save(job); // persist updated time
                jobQueue.offer(job); // enqueue
            } else {
                System.out.println("⏩ Not yet time to run.");
            }
        }

        System.out.println("=======================================");

        // Execute jobs in FIFO order
        while (!jobQueue.isEmpty()) {
            ScheduledJob job = jobQueue.poll();
            jobExecutor.execute(job); // this can be async or sync
        }
    }

    private int parseInterval(String cronExpr) {
        // Dummy logic assuming cronExpr is like "*/1 * * * *"
        try {
            String[] parts = cronExpr.split(" ");
            if (parts[0].startsWith("*/")) {
                return Integer.parseInt(parts[0].substring(2));
            }
        } catch (Exception ignored) {}
        return 1; // default fallback
    }
}
