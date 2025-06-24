package org.example.mybooklibrary.payment;

import lombok.RequiredArgsConstructor;
import org.example.mybooklibrary.book.BookRepository;
import org.example.mybooklibrary.book.Books;
import org.example.mybooklibrary.user.User;
import org.example.mybooklibrary.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public PaymentResponse createPayment(PaymentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Books book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setBook(book);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(request.getPaymentStatus());
        payment.setTransactionId(request.getTransactionId());
        payment.setCurrency(request.getCurrency());

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getUser().getName(),
                payment.getBook().getTitle(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTransactionId(),
                payment.getCurrency(),
                payment.getPaymentDate()
        );
    }
}
