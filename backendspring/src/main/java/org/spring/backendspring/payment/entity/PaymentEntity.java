package org.spring.backendspring.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.spring.backendspring.common.BasicTime;
<<<<<<< HEAD
import org.spring.backendspring.payment.PaymentStatus;
=======
>>>>>>> 016e24b (작업중인 변경사항)

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_tb")

<<<<<<< HEAD
<<<<<<< HEAD
public class PaymentEntity extends BasicTime {
=======
public class PaymentEntity extends BasicTime{
>>>>>>> 016e24b (작업중인 변경사항)
=======
public class PaymentEntity extends BasicTime{
>>>>>>> origin/1-4

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