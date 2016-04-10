package com.scu.scuWitkey.core.job;

import com.scu.scuWitkey.core.service.MissionService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

public class ValidateMissionJob implements Job {
    private Logger logger = LoggerFactory.getLogger(ValidateMissionJob.class);
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("ValidateMissionJob execute ---");
        long ms = System.currentTimeMillis();
        System.out.println("\t\t" + new Date(ms));
        SchedulerContext schedulerContext;
        try {
            schedulerContext = context.getScheduler().getContext();
            MissionService missionService = (MissionService)schedulerContext.get("missionServiceKey");
            missionService.updateMissionStatus();
        } catch (SchedulerException e) {
            logger.info("ValidateMissionJob execute SchedulerException ---- error =" + e.getMessage());
        }
    }
}
