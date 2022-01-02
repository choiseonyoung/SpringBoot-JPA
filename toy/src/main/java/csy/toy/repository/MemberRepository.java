package csy.toy.repository;

import csy.toy.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
// 컴포넌트 스캔 대상이 돼서 스프링 빈으로 등록됨
// JPA 예외를 스프링 기반 예외로 예외 변환
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext // JPA가 제공하는 표준 어노테이션
    private EntityManager em;
    // 이렇게 해주면 스프링이 엔티티메니저를 만들어서 여기에 주입해주게 됨
    // (스프링부트는 (정확히 말하면 spring data jpa) @Autowired 로 해도 인젝션되게 지원해줌)
    
    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    
    public List<Member> findAll() {
        // JPQL 작성. 첫번째에 JQPL, 두번째에 반환 타입 넣으면 됨
        // SQL과 좀 다름 : SQL은 테이블을 대상으로 쿼리, 이건 엔티티 객체를 대상으로 쿼리
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",
                        Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}