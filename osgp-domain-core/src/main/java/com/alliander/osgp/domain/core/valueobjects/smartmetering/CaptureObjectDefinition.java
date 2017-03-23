/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;
import java.util.Objects;

public class CaptureObjectDefinition implements Serializable {

    private static final long serialVersionUID = -1155157491281654400L;

    private final int classId;
    private final ObisCodeValues logicalName;
    private final byte attributeIndex;
    private final Integer dataIndex;

    public CaptureObjectDefinition(final int classId, final ObisCodeValues logicalName, final byte attributeIndex,
            final Integer dataIndex) {
        this.classId = classId;
        this.logicalName = logicalName;
        this.attributeIndex = attributeIndex;
        this.dataIndex = dataIndex;
    }

    @Override
    public String toString() {
        return String.format("{ %d, %s, %d, %d }", this.classId, this.logicalName, this.attributeIndex,
                this.dataIndex == null ? 0 : this.dataIndex);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CaptureObjectDefinition)) {
            return false;
        }
        final CaptureObjectDefinition other = (CaptureObjectDefinition) obj;
        return this.classId == other.classId && Objects.equals(this.logicalName, other.logicalName)
                && this.attributeIndex == other.attributeIndex && Objects.equals(this.dataIndex, other.dataIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.classId, this.logicalName, this.attributeIndex, this.dataIndex);
    }

    public int getClassId() {
        return this.classId;
    }

    public ObisCodeValues getLogicalName() {
        return this.logicalName;
    }

    public byte getAttributeIndex() {
        return this.attributeIndex;
    }

    public Integer getDataIndex() {
        return this.dataIndex;
    }
}