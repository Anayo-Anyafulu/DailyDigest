package junioranyafulu.DailyDigest.scheduler;

import junioranyafulu.DailyDigest.service.DailyDigestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@ConditionalOnProperty(name = "digest.scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class DigestScheduler {

    private final DailyDigestService dailyDigestService;

    @Autowired
    public DigestScheduler(DailyDigestService dailyDigestService) {
        this.dailyDigestService = dailyDigestService;
    }

    @Scheduled(cron = "${digest.scheduling.cron:0 0 8 * * *}")
    public void generateDailyDigest() {
        log.info("=== Starting scheduled daily digest generation ===");

        try {
            dailyDigestService.generateDailyDigest();
            log.info("=== Scheduled digest generation completed successfully ===");
        } catch (Exception e) {
            log.error("=== Scheduled digest generation failed ===", e);
        }
    }
}
