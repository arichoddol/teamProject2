package org.spring.backendspring.payment.dto;

import lombok.*;
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 하위 아이템 DTO 리스트 포함
    private List<PaymentItemDto> paymentItems;

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
                                .collect(Collectors.toList())
                        : null)
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
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .build();

        if (this.paymentItems != null) {
            entity.setPaymentItemEntities(
                    this.paymentItems.stream()
                            .map(itemDto -> {
                                PaymentItemEntity itemEntity = itemDto.toEntity();
                                itemEntity.setPayment(entity); // 양방향 관계 설정
                                return itemEntity;
                            })
                            .collect(Collectors.toList())
            );
        }

        return entity;
    }
}
