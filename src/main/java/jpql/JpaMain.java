package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            // FLUSH 자동호출
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            em.clear(); // 벌크연산 후 클리어를 해줘야 다시 조회하는 findMember 의 age 가 20으로 출력됨!

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember);

            // 0 0 0 출력 --> 이런 상황을 주의해야 함. 벌크연산은 영속성컨텍스트를 무시! DB 에만 반영됨!
            System.out.println("member1.getAge() = " + member1.getAge()); // 0
            System.out.println("member2.getAge() = " + member2.getAge()); // 0
            System.out.println("member3.getAge() = " + member3.getAge()); // 0

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
