package csy.toy.service;

import csy.toy.domain.Member;
import csy.toy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
// JPA의 모든 데이터 변경이나 로직들은 가급적이면 트랜잭션 안에서 다 실행돼야 함
public class MemberService {

    @Autowired
    // 스프링이 스프링 빈에 등록돼있는 멤버레파지토리를 인젝션 해 줌
    // 인젝션 하는 방법 여러가지 있는데, 일단 이걸 쓰고 뒤에 더 좋은 방법 알려주실 것
    MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    // 기본적으로 다른 메서드들에는 @Transactional(readOnly = true) 이 적용되고
    // 이 메서드는 따로 설정 (기본으로 @Transactional(readOnly = false) 가 먹힘)
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증하는 비즈니스 로직
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 간단하게 하기 위해 이 정도만
        List<Member> findMembers =
                memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        // 동시에
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}