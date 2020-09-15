package com.jrp.pma.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent>, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ContextClosedHandler.class);

    private ApplicationContext context;

    // graceful shutdown of executorservice https://blog.marcosbarbero.com/graceful-shutdown-spring-boot-apps/
    // https://programtalk.com/java/executorservice-not-shutting-down/
    // https://stackoverflow.com/questions/6603051/how-can-i-shutdown-spring-task-executor-scheduler-pools-before-all-other-beans-i

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Map<String, ThreadPoolTaskScheduler> schedulers = context.getBeansOfType(ThreadPoolTaskScheduler.class);

        for (ThreadPoolTaskScheduler scheduler : schedulers.values()) {
            scheduler.getScheduledExecutor().shutdown();
            try {
                scheduler.getScheduledExecutor().awaitTermination(20000, TimeUnit.MILLISECONDS);
                if(scheduler.getScheduledExecutor().isTerminated() || scheduler.getScheduledExecutor().isShutdown())
                    log.info("Scheduler "+scheduler.getThreadNamePrefix() + " has stoped");
                else{
                    log.info("Scheduler "+scheduler.getThreadNamePrefix() + " has not stoped normally and will be shut down immediately");
                    scheduler.getScheduledExecutor().shutdownNow();
                    log.info("Scheduler "+scheduler.getThreadNamePrefix() + " has shut down immediately");
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Map<String, ThreadPoolTaskExecutor> executers = context.getBeansOfType(ThreadPoolTaskExecutor.class);

        for (ThreadPoolTaskExecutor executor: executers.values()) {
            int retryCount = 0;
            while(executor.getActiveCount()>0 && ++retryCount<51){
                try {
                    log.info("Executer "+executor.getThreadNamePrefix()+" is still working with active " + executor.getActiveCount()+" work. Retry count is "+retryCount);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!(retryCount<51))
                log.info("Executer "+executor.getThreadNamePrefix()+" is still working.Since Retry count exceeded max value "+retryCount+", will be killed immediately");
            executor.shutdown();
            log.info("Executer "+executor.getThreadNamePrefix()+" with active " + executor.getActiveCount()+" work has killed");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}