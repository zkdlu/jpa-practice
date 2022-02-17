package jpa01;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member findMember = findMember(em, 1L);

            findMember.setName("안녕안녕");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();;
    }

    private static void deleteMember(EntityManager em, Member findMember) {
        em.remove(findMember);
    }

    private static void saveMember(EntityManager em, long id, String name) {
        Member member = new Member();
        member.setId(id);
        member.setName(name);

        em.persist(member);
    }

    private static Member findMember(EntityManager em, long id) {
        return em.find(Member.class, id);
    }
}
