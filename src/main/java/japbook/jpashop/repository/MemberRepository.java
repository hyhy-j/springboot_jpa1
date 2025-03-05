package japbook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import japbook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em; // 스프링 데이터 jpa가 Persistencecontext 말고 Autowired만 붙여도 되도록 지원

    public void save(Member member){
        em.persist(member);
        // 영속성 컨텍스트에 member 객체를 올림, id값이 영속성 컨텍스트의 key가 됨. db에 들어가지 않아도 생성...
        //persist한다고 바로 테이블에 insert 쿼리가 날라가는 것이 아님. 데이터 트랜잭션이 커밋될 때 플러시가 되면서 insert 쿼리가 나감.
    }

    public Member findOne(Long id){
        return em.find(Member.class, id); // 단건조회, 타입과 pk
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class) //sql은 테이블 대상, jpql은 엔티티 대상
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
