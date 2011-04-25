package com.abien.xray.business.store.boundary;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.Date;

/**
 * User: blog.adam-bien.com
 * Date: 25.02.11
 * Time: 21:25
 */
@Singleton
@Startup
public class ReferersFlushTimer {

    @Inject
    private int referersFlushRate;

    @EJB
    Hits hits;

    @Resource
    TimerService timerService;

    @PostConstruct
    public void initializeTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.minute("*/" + this.referersFlushRate).hour("*");
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerService.createCalendarTimer(expression, timerConfig);
    }

    @Timeout
    public void initiateFlush() {
        this.hits.persistReferersCache();
    }


}
