package com.daqem.jobsplus.fabric.scheduler;

import com.daqem.jobsplus.JobsPlus;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TickScheduler {

    private static boolean isRegistered = false;
    private static Map<Long, List<ScheduledTask>> tasks = new HashMap<>();

    private static long currentTick = 0L;

    public static void register() {
        if (isRegistered) return;
        isRegistered = true;

        ServerTickEvents.START_SERVER_TICK.register(TickScheduler::onTick);

    }

    private static void onTick(MinecraftServer server) {

        List<ScheduledTask> list = tasks.remove(currentTick);

        if(list != null) {
            for (ScheduledTask task : list) {
                try {
                    // Run the task
                    task.task.accept(task);

                    // reschedule the task if it has an interval
                    if(!task.isCancelled && task.interval > 0) {
                        schedule(task.interval, task);
                    }
                    // Complete the afterCompletion future
                    else if(task.afterCompletion != null) {
                        task.afterCompletion.complete(task);
                    }
                }
                catch (Exception e) {
                    JobsPlus.LOGGER.error("Exception occurred during scheduled task.", e);
                }
            }
        }

        currentTick++;
    }

    public static ScheduledTask scheduleNext(Consumer<ScheduledTask> task) {
        List<ScheduledTask> list = tasks.computeIfAbsent(currentTick + 1, $ -> new ArrayList<>());
        ScheduledTask t = new ScheduledTask(task, 0);
        list.add(t);
        return t;
    }

    public static ScheduledTask scheduleLater(long delay, Consumer<ScheduledTask> task) {
        List<ScheduledTask> list = tasks.computeIfAbsent(currentTick + Math.max(1, delay), $ -> new ArrayList<>());
        ScheduledTask t = new ScheduledTask(task, 0);
        list.add(t);
        return t;
    }

    public static ScheduledTask scheduleRepeating(long delay, long interval, Consumer<ScheduledTask> task) {
        List<ScheduledTask> list = tasks.computeIfAbsent(currentTick + Math.max(1L, delay), $ -> new ArrayList<>());
        ScheduledTask t = new ScheduledTask(task, interval);
        list.add(t);
        return t;
    }

    public static ScheduledTask schedule(long delay, ScheduledTask task) {
        List<ScheduledTask> list = tasks.computeIfAbsent(currentTick + Math.max(1L, delay), $ -> new ArrayList<>());
        list.add(task);
        return task;
    }

}
