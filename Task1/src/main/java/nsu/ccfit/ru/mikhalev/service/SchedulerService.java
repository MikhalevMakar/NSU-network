package nsu.ccfit.ru.mikhalev.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.model.CheckerHost;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Slf4j
public class SchedulerService implements Job  {
    private static CheckerHost interfaceHostCheck;

    public static void setHostCheck(CheckerHost hostCheck) {
        interfaceHostCheck = hostCheck;
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.info("scheduler execute");
        interfaceHostCheck.run();
    }
}
