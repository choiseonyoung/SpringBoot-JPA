package csy.toy.repository;

import csy.toy.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }
    
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }
    
    // 검색 (동적쿼리 들어가고 복잡하기 때문에 마지막에 따로 설명)
// public List<Order> findAll(OrderSearch orderSearch) { ... }
}