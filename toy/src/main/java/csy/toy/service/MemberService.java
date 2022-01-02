package csy.toy.service;

import csy.toy.domain.Member;
import csy.toy.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
// JPA의 모든 데이터 변경이나 로직들은 가급적이면 트랜잭션 안에서 다 실행돼야 함
//@AllArgsConstructor // 필드 가지고 생성자 만들어 주는 것 이것보다 한 단계 더 좋은 게 @RequiredArgsConstructor
@RequiredArgsConstructor // final 있는 필드만 가지고 생성자를 만들어 줌
public class MemberService {

    // 스프링이 스프링 빈에 등록돼있는 멤버레파지토리를 인젝션 해 줌
    // 인젝션 하는 방법 여러가지 있는데, 일단 이걸 쓰고 뒤에 더 좋은 방법 알려주실 것
    // [ 1번 방법 ]
//    @Autowired
    private final MemberRepository memberRepository;
    // 이 필드를 변경할 일이 없기 때문에 final 로 하는 걸 권장 (컴파일 시점에 체크를 해줄 수 있기 때문에 ex. 생성자 기껏 만들고 값 세팅 안 했을 때)

    // [ 2번 방법 ] setter 인젝션
    // 장점 : 테스트코드 같은 걸 작성할 때 Mock 같은 걸 직접 주입해줄 수 있음
    // 메서드를 통해 주입하니까 가짜 멤버레파지토리 같은 걸 주입할 수 있음
    // 단점 : 실제 애플리케이션 돌아가는 시점에 누군가가 이걸 바꿀 수 있음
    // 하지만 보통 이걸 바꿀 일이 없음
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 궁극적으로 요즘 권장하는 방법 : 생성자 인젝션
    // 한번 생성할 때 이게 완성이 돼버리기 때문에 중간에 set해서 멤버레파지토리를 바꿀 수 없음
    // 테스트 케이스 같은 걸 작성할 때 ~
    // 최신 버전 스프링에서는 생성자가 하나만 있는 경우에는 스프링이 @Autowired 가 없어도 자동으로 인젝션을 해줌
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

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
        // 똑같은 이름이 동시에 회원가입 하게 돼서 이 메서드를 통과할 수 있기 때문에
        // 실무에서는 한번 더 최후의 방어를 해야 함
        // 멀티 쓰레드나 이런 상황을 고려해서 데이터베이스에 member의 name을 유니크 제약조건으로 잡아주는 걸 권장
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