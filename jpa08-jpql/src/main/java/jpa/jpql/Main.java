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
            Team team = new Team();
            team.setName("team");
            em.persist(team);

            Member member = new Member();
            member.setName("lee");
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            subQuery(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void subQuery(EntityManager em) {
        // exists
        // where o > ALL(select ...)
        // where o > ANY(select ...)
        em.createQuery("select m from Member m where exists (select t from m.team t where t.name = 'team')", Member.class)
                .getResultList();
        em.createQuery("select m from Member m where m.age > (select avg(m2.age) from Member m2)", Member.class)
                .getResultList();
    }

    private static void join(EntityManager em) {
        em.createQuery("select m from Member m inner join m.team t", Member.class).getResultList();
        em.createQuery("select m from Member m left outer join m.team t on t.name = 'team'", Member.class).getResultList();
        em.createQuery("select m from Member m, Team t where m.name = t.name", Member.class).getResultList();
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
