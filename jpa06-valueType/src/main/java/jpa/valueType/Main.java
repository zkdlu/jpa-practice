package jpa.valueType;

import jpa.valueType.domain.Address;
import jpa.valueType.domain.Member;
import jpa.valueType.domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Address address = new Address("city", "street", "zip");

            Member member = new Member();
            member.setName("lee");
            member.setAddress(address);
            member.setPeriod(new Period());
            em.persist(member);

            Member member2 = new Member();
            member2.setName("lee");
            member2.setAddress(address);
            member2.setPeriod(new Period());
            em.persist(member2);

            address.setCity("newCity");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
