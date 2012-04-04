package com.abien.xray.business.scavenger.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.scavenger.control.RefererScavenger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Singleton
@Interceptors(PerformanceAuditor.class)
public class RefererScavengerTimer {

    @Inject
    private Instance<String> scavengerRuns;
    
    @Resource
    TimerService timerService;
    
    @Inject
    RefererScavenger refererScavenger;
    
    public void initializeTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.dayOfWeek(this.scavengerRuns.get());
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerService.createCalendarTimer(expression, timerConfig);
    }

    @Timeout
    public void garbageCollectReferers() {
        this.refererScavenger.removeInactiveReferers();
    }
}
