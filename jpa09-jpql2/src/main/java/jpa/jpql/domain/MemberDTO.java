package jpa.jpql.domain;

import lombok.Getter;

@Getter
public class MemberDTO {
    private String name;
    private int age;

    public MemberDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
