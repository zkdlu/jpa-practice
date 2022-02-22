package jpa.inheritance.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {
    @Id @GeneratedValue
    private Long id;
}
