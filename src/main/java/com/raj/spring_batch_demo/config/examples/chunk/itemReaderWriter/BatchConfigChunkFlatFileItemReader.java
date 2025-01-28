package com.raj.spring_batch_demo.config.examples.chunk.itemReaderWriter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@ConditionalOnExpression("'${batch.example}' == 'chunk.itemReaderFlatFile'")
public class BatchConfigChunkFlatFileItemReader {

    @Bean
    public ItemReader<Product> flatFItemReader() {
        FlatFileItemReader<Product> itemReader = new FlatFileItemReader<>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new ClassPathResource("/data/ProductDetails.csv"));
        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        return itemReader;
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Product, Product>chunk(3, transactionManager)
                .reader(flatFItemReader())
                .writer(chunk -> {
                    log.info("Item writer started.");
                    chunk.forEach(item -> {
                        log.info(item.toString());
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
