package org.example.mybooklibrary.Payment;

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

    private final PaymentsRepository paymentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public PaymentsResponseDto createPayment(PaymentsRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Books book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Payments payment = new Payments();
        payment.setUser(user);
        payment.setBooks(book);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(request.getPaymentStatus());
        payment.setTransactionId(request.getTransactionId());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentDate(request.getPaymentDate()); // Make sure to set payment date

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    public List<PaymentsResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentsResponseDto mapToResponse(Payments payment) {
        return PaymentsResponseDto.builder()
                .id(payment.getId())
                .username(payment.getUser().getUsername())
                .bookTitle(payment.getBooks().getTitle())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .currency(payment.getCurrency())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}