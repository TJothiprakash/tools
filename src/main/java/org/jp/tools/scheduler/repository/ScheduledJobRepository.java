package org.jp.tools.scheduler.repository;

import org.jp.tools.scheduler.model.ScheduledJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduledJobRepository extends JpaRepository<ScheduledJob, Long> {
    List<ScheduledJob> findByActiveTrue();
}
