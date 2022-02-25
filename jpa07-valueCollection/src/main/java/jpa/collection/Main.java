package jpa.collection;

import jpa.collection.domain.Address;
import jpa.collection.domain.Member;

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
            Member member = new Member();
            member.setName("lee");
            member.setAddress(new Address("city1", "street1", "zip1"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

            member.getAddressHistory().add(new Address("city2", "street2", "zip2"));
            member.getAddressHistory().add(new Address("city3", "street3", "zip3"));

            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = " + address.getCity());
            }

            // 값 타입은 식별자 개념이 없음
            // 컬렉션에 변경사항이 발생하면, 모든 데이터 삭제후 다시 저장함.
            // 모든 컬럼을 묶어서 기본 키를 구성해야 함
            // 가능하면 일대다 관계를 고려
            findMember.getAddressHistory().remove(new Address("city2", "street2", "zip2"));
            findMember.getAddressHistory().add(new Address("newCity", "newSteet", "newZip"));


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
