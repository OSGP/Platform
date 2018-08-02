package com.alliander.osgp.domain.core.valueobjects;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CdmaCommunicationSettings implements Serializable {

    private static final long serialVersionUID = 7540392130297372145L;

    @Column
    private String mast;
    @Column
    private Short batch;

    protected CdmaCommunicationSettings() {
        // Default constructor
    }

    public CdmaCommunicationSettings(final String mast, final Short batch) {
        this.mast = mast;
        this.batch = batch;
    }

    public String getMast() {
        return this.mast;
    }

    public Short getBatch() {
        return this.batch;
    }

    @Override
    public String toString() {
        return "CdmaCommunicationSettings [mast=" + this.mast + ", batch=" + this.batch + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mast, this.batch);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CdmaCommunicationSettings)) {
            return false;
        }

        final CdmaCommunicationSettings other = (CdmaCommunicationSettings) obj;
        return (Objects.equals(this.mast, other.mast) && Objects.equals(this.batch, other.batch));
    }

}
