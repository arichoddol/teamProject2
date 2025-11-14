package org.spring.backendspring.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.payment.PaymentStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_tb")

public class PaymentEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long memberId;

    private String paymentAddr;
    private String paymentMethod;
    private String paymentPost;
    private String paymentResult;
    private String paymentType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PaymentItemEntity> paymentItemEntities = new ArrayList<>();

    public Object getItems() {

        throw new UnsupportedOperationException("Unimplemented method 'getItems'");
    }
}