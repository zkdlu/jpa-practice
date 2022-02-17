package jpa01;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            List<Member> members = em.createQuery("select m from Member as m where m.id > 0", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            for (Member member : members) {
                System.out.println(member.getName());
            }

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
