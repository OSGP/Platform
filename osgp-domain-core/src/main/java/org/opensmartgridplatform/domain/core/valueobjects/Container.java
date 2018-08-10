package org.opensmartgridplatform.domain.core.valueobjects;

import java.io.Serializable;
import java.util.Objects;

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
        return Objects.hash(this.city, this.postalCode, this.street, this.number, this.municipality);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Container)) {
            return false;
        }

        final Container other = (Container) obj;
        return Objects.equals(this.city, other.city) && Objects.equals(this.postalCode, other.postalCode)
                && Objects.equals(this.street, other.street) && Objects.equals(this.number, other.number)
                && Objects.equals(this.municipality, other.municipality);
    }

}
