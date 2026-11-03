package com.daqem.jobsplus.fabric.scheduler;

import com.daqem.jobsplus.JobsPlus;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ScheduledTask {

    /**
     * The task that runs
     */
    final Consumer<ScheduledTask> task;

    /**
     * The ticks between each time the task runs
     */
    final long interval;

    /**
     * If cancelled, it will not run again, regardless of other intervals
     */
    boolean isCancelled = false;


    // future to run after the task is completed
    @Nullable CompletableFuture<ScheduledTask> afterCompletion = null;

    public ScheduledTask(Consumer<ScheduledTask> task, long interval) {
        this.task = task;
        this.interval = interval;
    }

    public CompletableFuture<ScheduledTask> thenTrigger(Consumer<ScheduledTask> consumer) {
        CompletableFuture<ScheduledTask> future = new CompletableFuture<>();

        future.thenAccept(consumer)
                .exceptionally(throwable -> {
                    JobsPlus.LOGGER.error("Exception in scheduled task completion callback.", throwable);
                    return null;
                });

        afterCompletion = future;
        return future;
    }

    public void cancel() {
        isCancelled = true;
    }
}
