/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProfileEntryVo implements Serializable {

    private static final long serialVersionUID = 991045734132231709L;

    private final String stringValue;
    private final Date dateValue;
    private final BigDecimal floatValue;
    private final Long longValue;

    public ProfileEntryVo(String stringValue, Date dateValue, BigDecimal floatValue, Long longValue) {
        super();
        this.stringValue = stringValue;
        this.dateValue = dateValue;
        this.floatValue = floatValue;
        this.longValue = longValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public Date getDateValue() {
        return this.dateValue;
    }

    public BigDecimal getFloatValue() {
        return this.floatValue;
    }

    public Long getLongValue() {
        return this.longValue;
    }

    public Object getValue() {
        if (this.stringValue != null) {
            return this.stringValue;
        } else if (this.dateValue != null) {
            return this.dateValue;
        } else if (this.longValue != null) {
            return this.longValue;
        } else {
            return this.floatValue;
        }
    }

}