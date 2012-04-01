package com.abien.xray.business.scavenger.boundary;

import com.abien.xray.business.scavenger.control.RefererScavenger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class RefererScavengerTimer {

    @Inject
    private int scavengerPeriodInHours;
    
    @Resource
    TimerService timerService;
    
    @Inject
    RefererScavenger refererScavenger;
    
    @PostConstruct
    public void initializeTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.hour("*/"+this.scavengerPeriodInHours);
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerService.createCalendarTimer(expression, timerConfig);
    }

    @Timeout
    public void garbageCollectReferers() {
        this.refererScavenger.removeInactiveReferers();
    }
}
