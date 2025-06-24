package org.example.mybooklibrary.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mybooklibrary.payment.Payment;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(nullable = false)
    private String Title;
    @Column(nullable = false)
    private String Author;
    @Column(nullable = false)
    private LocalDate PublishDate;
    @Column(nullable = false,unique = true)
    private String ISBN;
    private String category;
    private Boolean availabilityStatus=true ;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
