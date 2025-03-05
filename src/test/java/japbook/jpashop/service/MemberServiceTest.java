package japbook.jpashop.service;

import jakarta.persistence.EntityManager;
import japbook.jpashop.domain.Member;
import japbook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    //@Autowired EntityManager em;

    @Test //같은 트랜잭션 안에서 id(pk)가 똑같으면 같은 영속성 컨텍스트 안에서 똑같이 관리됨.
    @Rollback(false) //테스트에서의 트랜잭션은 무조건 롤백이기 때문에 insert 쿼리를 보려면 해당 옵션을 적용해줘야 함.
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        //em.flush(); //영속성 컨텍스트에 있는 데이터 변경 내용을 반영하는 것이 플러시
        //but 트랜잭션이 커밋되지는 않기 때문에 반영 X, DB에만 반영할 뿐..
        //트랜잭션이 커밋될 때 자동으로 flush()가 호출된다.
        Assert.assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 함.

        //then
        fail("예외가 발생해야 한다."); //위에서 예외가 발생하기 때문에 여기까지 오면 안 됨.
    }

}