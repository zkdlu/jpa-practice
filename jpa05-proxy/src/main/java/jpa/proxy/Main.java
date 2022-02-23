package jpa.proxy;

import jpa.proxy.domain.Member;
import jpa.proxy.domain.Team;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void nPlusOne(EntityManager em) {
        Team team = new Team();
        team.setName("team1");
        em.persist(team);

        Member m = new Member();
        m.setName("hello");
        m.setTeam(team);
        em.persist(m);

        em.flush();
        em.clear();

        List<Member> mebmers = em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    private static void lazyLoad(EntityManager em) {
        Team team = new Team();
        team.setName("team1");
        em.persist(team);

        Member m = new Member();
        m.setName("hello");
        m.setTeam(team);
        em.persist(m);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, m.getId());
        System.out.println("m = " + findMember.getName());
        System.out.println("t = " + findMember.getTeam().getName());
    }

    private static void proxyPractice(EntityManagerFactory emf, EntityManager em) {
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
    }
}
