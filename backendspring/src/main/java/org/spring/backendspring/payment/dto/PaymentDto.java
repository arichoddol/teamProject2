package org.spring.backendspring.payment.dto;

import lombok.*;
import org.spring.backendspring.payment.PaymentStatus;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    private Long paymentId;
    private Long memberId;
    private String paymentAddr;
    private String paymentMethod;
    private String paymentPost;
    private String paymentResult;
    private String paymentType;
    private PaymentStatus paymentStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private List<PaymentItemDto> paymentItems;

    private Long productPrice;
    private String tid;
    private String pgToken;
    private String paymentReadyJson;
    private int isSucceeded;

    // Entity → DTO 변환
    public static PaymentDto fromEntity(PaymentEntity entity) {
        if (entity == null) return null;

        return PaymentDto.builder()
                .paymentId(entity.getPaymentId())
                .memberId(entity.getMemberId())
                .paymentAddr(entity.getPaymentAddr())
                .paymentMethod(entity.getPaymentMethod())
                .paymentPost(entity.getPaymentPost())
                .paymentResult(entity.getPaymentResult())
                .paymentType(entity.getPaymentType())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .paymentItems(entity.getPaymentItemEntities() != null ?
                        entity.getPaymentItemEntities().stream()
                                .map(PaymentItemDto::fromEntity)
                                .collect(Collectors.toList()) : null)
                .paymentStatus(entity.getPaymentStatus())
                .productPrice(entity.getProductPrice())
                .tid(entity.getTid())
                .pgToken(entity.getPgToken())
                .paymentReadyJson(entity.getPaymentReadyJson())
                .isSucceeded(entity.getIsSucceeded())
                .build();
    }

    // DTO → Entity 변환
    public PaymentEntity toEntity() {
        PaymentEntity entity = PaymentEntity.builder()
                .paymentId(this.paymentId)
                .memberId(this.memberId)
                .paymentAddr(this.paymentAddr)
                .paymentMethod(this.paymentMethod)
                .paymentPost(this.paymentPost)
                .paymentResult(this.paymentResult)
                .paymentType(this.paymentType)
                .paymentStatus(this.paymentStatus)
                .productPrice(this.productPrice)
                .tid(this.tid)
                .pgToken(this.pgToken)
                .paymentReadyJson(this.paymentReadyJson)
                .isSucceeded(this.isSucceeded)
                .build();

        if (this.paymentItems != null) {
            entity.setPaymentItemEntities(
                    this.paymentItems.stream()
                            .map(itemDto -> {
                                PaymentItemEntity itemEntity = itemDto.toEntity();
                                itemEntity.setPayment(entity);
                                return itemEntity;
                            }).collect(Collectors.toList()));
        }
        return entity;
    }

    // toDto 추가
    public static PaymentDto toDto(PaymentEntity entity) {
        return fromEntity(entity);
    }
}
