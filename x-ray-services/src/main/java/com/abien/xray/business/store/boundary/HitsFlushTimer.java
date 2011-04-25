package com.abien.xray.business.store.boundary;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;

/**
 * User: blog.adam-bien.com
 * Date: 25.02.11
 * Time: 21:25
 */
@Singleton
@Startup
public class HitsFlushTimer {

    @Inject
    private int hitsFlushRate;

    @EJB
    Hits hits;

    @Resource
    TimerService timerService;

    @PostConstruct
    public void initializeTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.minute("*/" + this.hitsFlushRate).hour("*");
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerService.createCalendarTimer(expression, timerConfig);
    }

    @Timeout
    public void initiateFlush() {
        this.hits.persistHitsCache();
    }
}
