package com.raj.spring_batch_demo.config.examples.chunk.itemReaderWriter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
@ConditionalOnExpression("'${batch.example}' == 'chunk.itemReader'")
public class BatchConfigChunkItemReader {

    @Bean
    public ItemReader<String> itemReader() {
        List<String> productList = List.of("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8");
        return new ProductNameItemReader(productList);
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(itemReader())
                .writer(chunk -> {
                    log.info("Item writer started.");
                    chunk.forEach(item -> {
                        log.info(item);
                    });
                    log.info("Item writer complete.");
                })
                .build();

    }

    @Bean
    public Job job1(JobRepository jobRepository,
                    PlatformTransactionManager transactionManager) {
        return new JobBuilder("job1", jobRepository)
                .start(step1(jobRepository, transactionManager))
                .build();
    }
}
