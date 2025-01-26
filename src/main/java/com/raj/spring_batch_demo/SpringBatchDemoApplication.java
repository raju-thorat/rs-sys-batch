package com.raj.spring_batch_demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SpringBatchDemoApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(SpringBatchDemoApplication.class, args);
    }

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("job1")
    Job job;

    @Value("${batch.example}")
    public String exampleRun;

    @Override
    public void run(String... args) throws Exception {
        log.info("Example Run Type :{}", exampleRun);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("i", 2l)
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
