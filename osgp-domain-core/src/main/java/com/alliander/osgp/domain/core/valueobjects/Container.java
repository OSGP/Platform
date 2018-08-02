package com.alliander.osgp.domain.core.valueobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Container implements Serializable {

    private static final long serialVersionUID = -4694887724278796555L;

    @Column(length = 255)
    private String city;
    @Column(length = 10)
    private String postalCode;
    @Column(length = 255)
    private String street;
    @Column(length = 255)
    private String number;
    @Column(length = 255)
    private String municipality;

    public Container() {
        // Default constructor
    }

    public Container(final String city, final String postalCode, final String street, final String number,
            final String municipality) {
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.number = number;
        this.municipality = municipality;
    }

    public String getCity() {
        return this.city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getStreet() {
        return this.street;
    }

    public String getNumber() {
        return this.number;
    }

    public String getMunicipality() {
        return this.municipality;
    }

    @Override
    public String toString() {
        return "Container [city=" + this.city + ", postalCode=" + this.postalCode + ", street=" + this.street
                + ", number=" + this.number + ", municipality=" + this.municipality + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.city == null) ? 0 : this.city.hashCode());
        result = prime * result + ((this.municipality == null) ? 0 : this.municipality.hashCode());
        result = prime * result + ((this.number == null) ? 0 : this.number.hashCode());
        result = prime * result + ((this.postalCode == null) ? 0 : this.postalCode.hashCode());
        result = prime * result + ((this.street == null) ? 0 : this.street.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Container other = (Container) obj;
        if (this.city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!this.city.equals(other.city)) {
            return false;
        }
        if (this.municipality == null) {
            if (other.municipality != null) {
                return false;
            }
        } else if (!this.municipality.equals(other.municipality)) {
            return false;
        }
        if (this.number == null) {
            if (other.number != null) {
                return false;
            }
        } else if (!this.number.equals(other.number)) {
            return false;
        }
        if (this.postalCode == null) {
            if (other.postalCode != null) {
                return false;
            }
        } else if (!this.postalCode.equals(other.postalCode)) {
            return false;
        }
        if (this.street == null) {
            if (other.street != null) {
                return false;
            }
        } else if (!this.street.equals(other.street)) {
            return false;
        }
        return true;
    }

}
