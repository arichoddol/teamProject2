package org.spring.backendspring.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_tb")

public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long memberId;

    private String paymentAddr;
    private String paymentMethod;
    private String paymentPost;
    private String paymentResult;
    private String paymentType;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PaymentItemEntity> paymentItemEntities = new ArrayList<>();

    public Object getItems() {

        throw new UnsupportedOperationException("Unimplemented method 'getItems'");
    }
}