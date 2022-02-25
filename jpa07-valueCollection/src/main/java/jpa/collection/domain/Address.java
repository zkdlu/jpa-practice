package jpa.collection.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.Objects;

@NoArgsConstructor
@Getter @Setter
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zip;

    public Address(String city, String street, String zip) {
        this.city = city;
        this.street = street;
        this.zip = zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zip, address.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zip);
    }
}
