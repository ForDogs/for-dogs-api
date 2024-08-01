package com.fordogs.product.batch;

import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class StockAlertScheduler {

    private final JobLauncher jobLauncher;
    private final Job stockAlertJob;

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleStockAlertJob() {
        JobParameters jobParameters = createJobParameters();
        runJob(jobParameters);
    }

    private JobParameters createJobParameters() {
        return new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
    }

    private void runJob(JobParameters jobParameters) {
        try {
            jobLauncher.run(stockAlertJob, jobParameters);
        } catch (Exception e) {
            throw GlobalErrorCode.internalServerException("상품 재고 알림 배치 작업 실행 중 오류가 발생하였습니다.");
        }
    }
}
