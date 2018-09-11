/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.core.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import org.opensmartgridplatform.core.application.tasks.ScheduledTaskScheduler;
import org.opensmartgridplatform.shared.application.config.AbstractConfig;

@EnableScheduling
@Configuration
@PropertySource("classpath:osgp-core.properties")
@PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true)
@PropertySource(value = "file:${osgp/Core/config}", ignoreResourceNotFound = true)
public class SchedulingConfig extends AbstractConfig implements SchedulingConfigurer {

    private static final String PROPERTY_NAME_SCHEDULING_SCHEDULED_TASKS_CRON_EXPRESSION = "scheduling.scheduled.tasks.cron.expression";
    private static final String PROPERTY_NAME_SCHEDULING_TASK_SCHEDULER_POOL_SIZE = "scheduling.task.scheduler.pool.size";
    private static final String PROPERTY_NAME_SCHEDULING_TASK_SCHEDULER_THREAD_NAME_PREFIX = "scheduling.task.scheduler.thread.name.prefix";

    private static final String PROPERTY_NAME_SCHEDULING_TASK_PAGE_SIZE = "scheduling.task.page.size";

    @Autowired
    private ScheduledTaskScheduler scheduledTaskScheduler;

    @Override
    public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(this.taskScheduler());
        taskRegistrar.addCronTask(new CronTask(this.scheduledTaskScheduler, this.scheduledTasksCronTrigger()));
    }

    public CronTrigger scheduledTasksCronTrigger() {
        final String cron = this.environment
                .getRequiredProperty(PROPERTY_NAME_SCHEDULING_SCHEDULED_TASKS_CRON_EXPRESSION);
        return new CronTrigger(cron);
    }

    @Bean(destroyMethod = "shutdown")
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(Integer
                .parseInt(this.environment.getRequiredProperty(PROPERTY_NAME_SCHEDULING_TASK_SCHEDULER_POOL_SIZE)));
        taskScheduler.setThreadNamePrefix(
                this.environment.getRequiredProperty(PROPERTY_NAME_SCHEDULING_TASK_SCHEDULER_THREAD_NAME_PREFIX));
        taskScheduler.setWaitForTasksToCompleteOnShutdown(false);
        return taskScheduler;
    }

    @Bean
    public Integer scheduledTaskPageSize() {
        return Integer.parseInt(this.environment.getRequiredProperty(PROPERTY_NAME_SCHEDULING_TASK_PAGE_SIZE));
    }
}
