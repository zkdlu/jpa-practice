package jpa.inheritance;

import jpa.inheritance.domain.Member;
import jpa.inheritance.domain.Team;

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
            Team team = new Team();
            team.setName("team 1");
            em.persist(team);

            System.out.println("==========");

            Member member = new Member();
            member.setName("건");
            member.setTeam(team);
            em.persist(member);

            em.flush(); // 영속성 컨텍스트에 있는걸 db에 쿼리를 날려 싱크 반영
            em.clear(); // 영속성 컨텍스트 초기화

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            System.out.println("findTeam = " + findTeam.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();;
    }
}
