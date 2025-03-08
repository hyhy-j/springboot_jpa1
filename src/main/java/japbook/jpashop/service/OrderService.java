package japbook.jpashop.service;

import japbook.jpashop.domain.Delivery;
import japbook.jpashop.domain.Member;
import japbook.jpashop.domain.Order;
import japbook.jpashop.domain.OrderItem;
import japbook.jpashop.domain.item.Item;
import japbook.jpashop.repository.ItemRepository;
import japbook.jpashop.repository.MemberRepository;
import japbook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //cascade 옵션에 의해 order를 persist하면 orderItem, delivery도 persist됨.
        //완전히 한 엔티티가 다른 엔티티의 주도권을 쥐고 있을 때, 다른 곳에서는 참조 X 일 때 옵션 사용
        orderRepository.save(order);

        return order.getId();
    }

    //취소
    @Transactional
    public void cancleOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel(); //더티 체킹 - 변경된 내용들을 jpa가 알아서 쿼리를 날려 적용해줌.
    }

    //주문 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
