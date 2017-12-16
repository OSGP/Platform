/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.publiclighting.application.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.alliander.osgp.adapter.domain.publiclighting.application.tasks.DeviceConnectionScheduledTask;
import com.alliander.osgp.shared.application.config.AbstractConfig;

@EnableScheduling
@Configuration
@PropertySources({ @PropertySource("classpath:osgp-adapter-domain-publiclighting.properties"),
        @PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true),
        @PropertySource(value = "file:${osgp/Core/config}", ignoreResourceNotFound = true), })
public class SchedulingConfigForDeviceConnectionScheduledTask extends AbstractConfig implements SchedulingConfigurer {

    private static final String PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_CRON_EXPRESSION = "scheduling.task.device.connection.cron.expression";
    private static final String PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_POOL_SIZE = "scheduling.task.device.connection.pool.size";
    private static final String PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_MANUFACTURER_NAME = "scheduling.task.device.connection.manufacturer.name";
    private static final String PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_MAX_ALLOWED_AGE = "scheduling.task.device.connection.max.allowed.age";

    @Autowired
    private DeviceConnectionScheduledTask deviceConnectionScheduledTask;

    @Override
    public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(this.deviceConnectionTaskScheduler());
        taskRegistrar.addCronTask(
                new CronTask(this.deviceConnectionScheduledTask, this.deviceConnectionScheduledTaskCronTrigger()));
    }

    public CronTrigger deviceConnectionScheduledTaskCronTrigger() {
        final String cron = this.environment
                .getRequiredProperty(PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_CRON_EXPRESSION);
        return new CronTrigger(cron);
    }

    @Bean(destroyMethod = "shutdown")
    public Executor deviceConnectionTaskScheduler() {
        final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(
                this.getNonRequiredIntegerPropertyValue(PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_POOL_SIZE, 10));
        taskScheduler.setThreadNamePrefix("osgp-adapter-domain-publiclighting-device-connection-scheduled-task-");
        taskScheduler.setWaitForTasksToCompleteOnShutdown(false);
        return taskScheduler;
    }

    @Bean
    public String deviceConnectionScheduledTaskManufacturerName() {
        return this.environment.getRequiredProperty(PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_MANUFACTURER_NAME);
    }

    @Bean
    public int deviceConnectionScheduledTaskMaximumAllowedAge() {
        return Integer.parseInt(
                this.environment.getRequiredProperty(PROPERTY_NAME_SCHEDULING_TASK_DEVICE_CONNECTION_MAX_ALLOWED_AGE));
    }
}