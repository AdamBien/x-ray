package com.abien.xray.business.scavenger.boundary;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Startup
@Singleton
public class ScavengerStarter {
    
    @Resource
    TimerService timerService;
    
    @Inject
    RefererScavengerTimer scavengerTimer;
    
    private long startDelay = 60*1000;
    private Timer timer;
    
    @PostConstruct
    public void initializeTimer(){
        TimerConfig config = new TimerConfig();
        config.setPersistent(false);
        this.timer = timerService.createSingleActionTimer(startDelay, config);
    }
    
    @Timeout
    public void startScavenger(){
        scavengerTimer.initializeTimer();
    }
    
    @PreDestroy
    public void cleanupTimer(){
        this.timer.cancel();
    }
}
