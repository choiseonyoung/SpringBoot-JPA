package csy.toy.service;

import static org.junit.Assert.*;

import csy.toy.domain.Member;
import csy.toy.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest // 이거 없으면 아래 @Autowired 다 실패함
@Transactional
// 이 스프링 어노테이션이 테스트 케이스에 있으면 기본적으로 ROLLBACK 해버림 (테스트가 반복돼야하기 때문에 DB에 데이터가 남으면 안 되기 때문에)
// 서비스 클래스나 레파지토리 등에 붙이면 당연히 ROLLBACK 하지 않음
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    // ** 롤백이지만 그래도 DB에 쿼리 날리는 걸 보고 싶다면

    @Test
    public void 회원가입() throws Exception {
        //Given // 이런 게 주어졌을 때
        Member member = new Member();
        member.setName("kim");
        
        //When // 이렇게 하면 (이걸 실행하면)
        Long saveId = memberService.join(member);
        
        //Then // 이렇게 된다 (결과가 이렇게 나와야 한다)
//        em.flush(); // ** 이렇게 설정. flush하면 영속성 컨텍스트에 있는걸 DB에 강제로 쿼리 날림. 그 다음에 스프링 테스트가 끝날 때 트랜잭셔널이 롤백을 해버림
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class) // 괄호 안에 이것들 넣어주면 try catch 쓴 지저분한 코드 지울 수 있다
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //When
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다.

        //Then
        fail("예외가 발생해야 한다.");
    }
}