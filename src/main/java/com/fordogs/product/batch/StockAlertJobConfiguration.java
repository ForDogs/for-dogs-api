package com.fordogs.product.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class StockAlertJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StockAlertTasklet stockAlertTasklet;

    @Bean(name = "stockAlertJob")
    public Job stockAlertJob() {
        return new JobBuilder("stockAlertJob", jobRepository)
                .start(checkStockStep())
                .build();
    }

    @Bean
    public Step checkStockStep() {
        return new StepBuilder("checkStockStep", jobRepository)
                .tasklet(stockAlertTasklet, transactionManager)
                .build();
    }
}
