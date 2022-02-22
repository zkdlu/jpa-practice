package jpa.inheritance;

import jpa.inheritance.domain.Member;
import jpa.inheritance.domain.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Movie movie = new Movie();
            movie.setActor("actor");
            movie.setDirector("director");
            movie.setPrice(100);
            movie.setName("name");

            em.persist(movie);

            em.flush();
            em.clear();

            System.out.println("===============================");
            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMovie = " + findMovie);

            Member member = new Member();
            member.setCreatedBy("lee");
            member.setCreatedDate(LocalDateTime.now());
            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();;
    }
}
