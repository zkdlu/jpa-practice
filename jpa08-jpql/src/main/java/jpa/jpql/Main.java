package jpa.jpql;

import jpa.jpql.domain.Member;
import jpa.jpql.domain.MemberDTO;
import jpa.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setName("lee" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            paging(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void paging(EntityManager em) {
        List<Member> members = em.createQuery("select m from Member m order by m.age desc", Member.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();
        System.out.println(members.size());
        for (Member mem : members) {
            System.out.println("mem = " + mem.getAge());
        }
    }


    private static void jpql(EntityManager em) {
        TypedQuery<Member> query = em.createQuery("select m from Member m where m.name = :name", Member.class);
        query.setParameter("name", "lee");
        Member singleResult = query.getSingleResult();
        System.out.println("singleResult.getName() = " + singleResult.getName());

        List<Member> members = query.getResultList();
        for (Member m : members) {
            System.out.println("m.getName() = " + m.getName());
        }
    }

    private static void projection(EntityManager em) {
        List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class)
                .getResultList();
    }

    private static void projection2(EntityManager em) {
        List<MemberDTO> resultList = em.createQuery("select new jpa.jpql.domain.MemberDTO(m.name, m.age) from Member m", MemberDTO.class)
                .getResultList();

        for (MemberDTO memberDTO : resultList) {
            System.out.println("memberDTO.getName() = " + memberDTO.getName());
            System.out.println("memberDTO.getName() = " + memberDTO.getAge());
        }
    }
}
