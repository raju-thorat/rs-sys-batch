package com.raj.spring_batch_demo.config.examples.conditional.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.lang.Nullable;

public class MyStepExecutionListener implements StepExecutionListener {

    @Nullable
    public ExitStatus afterStep(StepExecution stepExecution) {
        return new ExitStatus("TEST_STATUS");
    }

}
