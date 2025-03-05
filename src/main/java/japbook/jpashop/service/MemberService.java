package japbook.jpashop.service;

import japbook.jpashop.domain.Member;
import japbook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//javax것보다 스프링 Transaction을 사용하자.
@Transactional(readOnly = true) //조회 시 성능을 더 좋게 하는 옵션 - 플러시,더티체크X / 읽기용 데베 드라이버?리소스..
@RequiredArgsConstructor //final이 붙은 필드에 생성자를 만들어 줌!
public class MemberService {

    //필드주입은 아예 바꿀 수 없다는 것(테스트 시), setter주입은 세터를 통해 바뀔 수 있다는 점 ;; -> 생성자 주입
    private final MemberRepository memberRepository; // final은 바뀔 수 없기에 값 setting이 되어야 함.

    //회원 가입
    @Transactional
    public Long join(Member member){

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // 멀티스레드 상에서 동시에 같은 이름의 사용자가 가입할 경우 오류가 생김.
    // 실무에서는 이름에 unique 조건을 달아야 함.
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
