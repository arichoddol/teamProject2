// // package org.spring.backendspring;

// // import org.junit.jupiter.api.Test;
// // import org.spring.backendspring.payment.entity.PaymentEntity;
// // import org.spring.backendspring.payment.repository.PaymentRepository;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.boot.test.context.SpringBootTest;

// // import java.time.LocalDateTime;

// // @SpringBootTest
// // public class PaymentTest {

// //     @Autowired
// //     private PaymentRepository paymentRepository;

// //     @Test
// //     public void insertDummyPayments() {

// //         for (int i = 1; i <= 25; i++) {

<<<<<<< HEAD
<<<<<<< HEAD
        //     PaymentEntity payment = PaymentEntity.builder()
        //             .memberId((long) i)
        //             .paymentAddr("서울시 테스트로 " + i + "길")
        //             .paymentMethod(i % 2 == 0 ? "CARD" : "BANK")
        //             .paymentPost(String.format("%05d", 10000 + i))
        //             .paymentResult(i % 3 == 0 ? "FAIL" : "SUCCESS")
        //             .paymentType(i % 2 == 0 ? "ORDER" : "SUBSCRIBE")
        //             // .createTime(LocalDateTime.now())
        //             // .updateTime(LocalDateTime.now())
        //             .paymentItemEntities(null) // 혹은 new ArrayList<>()
        //             .build();
=======
=======
// <<<<<<< HEAD
//             PaymentEntity payment = PaymentEntity.builder()
//                     .memberId((long) i)
//                     .paymentAddr("서울시 테스트로 " + i + "길")
//                     .paymentMethod(i % 2 == 0 ? "CARD" : "BANK")
//                     .paymentPost(String.format("%05d", 10000 + i))
//                     .paymentResult(i % 3 == 0 ? "FAIL" : "SUCCESS")
//                     .paymentType(i % 2 == 0 ? "ORDER" : "SUBSCRIBE")
//                     .paymentItemEntities(null) // 혹은 new ArrayList<>()
//                     .build();

>>>>>>> 6331803c5767cdf2bd4f939bb03b40675001e617
//             PaymentEntity payment = PaymentEntity.builder()
//                     .memberId((long) i)
//                     .paymentAddr("서울시 테스트로 " + i + "길")
//                     .paymentMethod(i % 2 == 0 ? "CARD" : "BANK")
//                     .paymentPost(String.format("%05d", 10000 + i))
//                     .paymentResult(i % 3 == 0 ? "FAIL" : "SUCCESS")
//                     .paymentType(i % 2 == 0 ? "ORDER" : "SUBSCRIBE")
//                     // .createTime(LocalDateTime.now())
//                     // .updateTime(LocalDateTime.now())
//                     .paymentItemEntities(null) // 혹은 new ArrayList<>()
//                     .build();
>>>>>>> ed23335da8597b73225241b09e0fdab56104b9bc


// //             paymentRepository.save(payment);
// //         }

// //         System.out.println("✅ 25개의 더미 결제 데이터가 성공적으로 저장되었습니다!");
// //     }
// // }
