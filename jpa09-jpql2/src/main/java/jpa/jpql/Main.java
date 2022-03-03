package jpa.jpql;

import jpa.jpql.domain.Member;
import jpa.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("A");
            em.persist(teamA);
            Team teamB = new Team();
            teamB.setName("B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setName("mem1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("mem2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("mem3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

//            pathExpression(em);

//            fetchJoin(em);

            collectionFetchJoin(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void collectionFetchJoin(EntityManager em) {
        List<Team> teams = em.createQuery("select t from Team t join fetch t.members", Team.class).getResultList();
        for (Team team : teams) {
            System.out.println("team.getName() = " + team.getName() + ", " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("====> member.getName() = " + member.getName());
            }
        }
        System.out.println("=============================");
        teams = em.createQuery("select distinct t from Team t join fetch t.members", Team.class).getResultList();
        for (Team team : teams) {
            System.out.println("team.getName() = " + team.getName() + ", " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("====> member.getName() = " + member.getName());
            }
        }
        System.out.println("=============================");
        Set<Team> teamSets = em.createQuery("select t from Team t join fetch t.members", Team.class).getResultList().stream().collect(Collectors.toSet());
        for (Team team : teamSets) {
            System.out.println("team.getName() = " + team.getName() + ", " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("====> member.getName() = " + member.getName());
            }
        }
    }

    private static void fetchJoin(EntityManager em) {
        List<Member> mems = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member mem : mems) {
            System.out.println("mem.getName() = " + mem.getName() + ", " + mem.getTeam().getName());
        }
        System.out.println("=============================");
        mems = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();
        for (Member mem : mems) {
            System.out.println("mem.getName() = " + mem.getName() + ", " + mem.getTeam().getName());
        }
    }

    private static void pathExpression(EntityManager em) {
        // 상태 필드. 탐색 끝
        String query = "select m.name from Member m";
        em.createQuery(query).getResultList();
        // 묵시적 내부 조인 (묵시적 쓰면 튜닝 어려움, 쓰지마셈
        query = "select m.team from Member m";
        em.createQuery(query).getResultList();
        // 명시적 조인
        query = "select m.name from Team t join t.members m";
        em.createQuery(query).getResultList();
    }
}
