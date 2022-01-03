package csy.toy.domain;

import csy.toy.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 주문 상품

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 주문

    private int orderPrice; // 주문 (당시) 가격

    private int count; // 주문 (당시) 수량

    //==생성 메서드==//
    // item의 가격 아닌 orderPrice 따로 받는 이유 : 쿠폰값 할인 등으로 바뀔 수 있으니 따로 하는 게 맞음 (지금은 단순하게 해서 할인같은 거 없지만)
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    // 재고수량 원복
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    /** 주문 상품 전체 가격 조회 */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}