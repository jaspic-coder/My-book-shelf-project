package org.example.mybooklibrary.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mybooklibrary.Payment.Payments;

import java.time.LocalDate;
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
    private String imageUrl;
    @ManyToOne
    private Payments payments;

}
