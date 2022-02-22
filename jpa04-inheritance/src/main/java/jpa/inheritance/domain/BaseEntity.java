package jpa.inheritance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
public abstract class BaseEntity {
    private String createdBy;
    @Column(name = "createdAt")
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    @Column(name = "updatedAt")
    private LocalDateTime lastModifiedDate;
}
