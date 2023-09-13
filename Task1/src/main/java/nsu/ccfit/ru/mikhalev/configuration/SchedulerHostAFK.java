package nsu.ccfit.ru.mikhalev.configuration;

import nsu.ccfit.ru.mikhalev.service.SchedulerService;
import org.quartz.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.TIMEOUT;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.TIMEOUT_SECOND;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerHostAFK {
    public static void execution() throws SchedulerException {
        SchedulerFactory schedulerFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler scheduler = schedulerFact.getScheduler();

        scheduler.start();

        JobDetail job = newJob(SchedulerService.class)
            .withIdentity("JobCheckAFK", "group21211")
            .build();

        Trigger trigger = newTrigger()
            .withIdentity("TriggerMulticast", "group21211")
            .startNow()
            .withSchedule(simpleSchedule()
                .withIntervalInSeconds(TIMEOUT_SECOND)
                .repeatForever())
            .build();

        scheduler.scheduleJob(job, trigger);
    }
}
