package jpa.proxy;

import jpa.proxy.domain.Member;
import org.hibernate.Hibernate;

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
            Member member = new Member();
            member.setName("hello");
            em.persist(member);

            Member member2 = new Member();
            member2.setName("hello");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member.getId());
            Member m2 = em.getReference(Member.class, member2.getId());

            System.out.println("m1 -> " + m1.getName());
            System.out.println("m2 -> " + m2.getName());

            System.out.println("m1 == m2 : " + (m1 == m2));
            System.out.println("m1 : " + (m1 instanceof Member));
            System.out.println("m2  : " + (m2 instanceof Member));

            Member refMember = em.getReference(Member.class, member.getId());
            System.out.println("refMember = " + refMember.getClass());

            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember = " + findMember.getClass());

            System.out.println("ref == find : " + (refMember == findMember));

            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));
            Hibernate.initialize(refMember); // JPA 표준 아님
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();;
    }
}
