package jpa.collection.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/*
식별자가 필요하고 지속해서 값을 추적,변경해야 한다면 엔티티
 */
@Entity
@Table(name = "ADDRESS_ENTITY")
public class AddressEntity {
    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    public AddressEntity(Long id, Address address) {
        this.id = id;
        this.address = address;
    }
}
