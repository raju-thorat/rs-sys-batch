package com.raj.spring_batch_demo.config.examples.conditional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@ConditionalOnExpression("'${batch.example}' == 'conditional.decider'")
public class BatchConfigConditionalJobExecutionDecider {

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            log.info("step1 executed");
                            return RepeatStatus.FINISHED;
                        }, transactionManager)
                .build();

    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            boolean isSuccess = true;
                            if (!isSuccess) {
                                throw new RuntimeException("Test Exception");
                            }
                            log.info("step2 executed");
                            return RepeatStatus.FINISHED;
                        }, transactionManager)
                .build();

    }

    @Bean
    public Step step3(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            log.info("step3 executed");
                            return RepeatStatus.FINISHED;
                        }, transactionManager)
                .build();

    }

    @Bean
    public Step step4(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step4", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            log.info("step4 executed");
                            return RepeatStatus.FINISHED;
                        }, transactionManager)
                .build();

    }

    @Bean
    public Job job1(JobRepository jobRepository,
                    PlatformTransactionManager transactionManager) {
        return new JobBuilder("job1", jobRepository)
                .start(step1(jobRepository, transactionManager))
                .on(BatchStatus.COMPLETED.name()).to(myDecider())
                .on("TEST_STATUS").to(step2(jobRepository, transactionManager))
                .from(myDecider())
                .on("*").to(step3(jobRepository, transactionManager))
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider myDecider() {
        return new JobExecutionDecider() {
            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                return new FlowExecutionStatus("TEST_STATUS");
            }
        };
    }
}
