/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.microgrids.repositories;

import org.springframework.data.repository.CrudRepository;

import com.alliander.osgp.domain.microgrids.entities.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Task findByTaskIdentification(String taskIdentification);
}
