package csy.toy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    // mappedBy : 나는 연관관계 주인이 아니라 연관관계 거울이라는 의미
    // "member" : order 테이블에 있는 "member" 필드에서 매핑 된거라는 의미
    // 읽기 전용이 되는 것. 여기에 값을 넣는다고 해서 폴인키 값이 변경되지는 않는다
    private List<Order> orders = new ArrayList<>();
}