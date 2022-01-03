package csy.toy.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문 시간
    // LocalDateTime : 자바8 (예전에 Date 쓸 땐 어노테이션 매핑? 했어야 했는데 이건 그냥 쓰면 됨. 하이버네이트가 자동으로 해줌)

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    //==연관관계 메서드==//
    // 양방향일 때 쓰면 좋습니다. 양쪽 세팅하는 걸 원자적으로 한 코드로 딱 해결
    // 핵심적으로 컨트롤 하는 쪽에 들고 있는 게 좋다
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    
//    원래는 이 코든데 위 코드로 짤 수 있다
//    public static void main(String[] args) {
//        Member member = new Member();
//        Order order = new Order();
//
//        member.getOrders().add(order);
//        order.setMember(member);
//    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    //==생성 메서드==//
    // oderItem, delivery 등도 생성돼야 하고 여러 연관 관계 들어가고 복잡해짐. 이런 복잡한 생성은 별도의 생성 메서드가 있으면 좋음
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { // * ... : 가변 인자. 0부터 여러개까지 매개변수로 올 수 있음
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    // 실무에서는 훨씬 복잡
    // OrderItem 도 저렇게 넘어오는 게 아니라 파라미터나 DTO가 복잡하게 넘어옴. 또는 안에서 OrderItem을 생성해서 넣을 수도 있고.

    //==비즈니스 로직==//
    /** 주문 취소 */
    // 주문 취소 (CANCEL) 버튼 누르면 재고 다시 올려주는 거
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /** 전체 주문 가격 조회 */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
        // 스트림이나 람다 활용하면 더 깔끔하게 작성할 수 있음

        // 스트림으로 바꾸면 이렇게
//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();

    }

}